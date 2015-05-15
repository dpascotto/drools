package it.dpascotto.drools.common;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.kie.api.KieBase;
import org.kie.api.KieBaseConfiguration;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

@Service("drools-service")
public class DroolsService {
	Logger log = Logger.getLogger(DroolsService.class);
	
	private KieBase kieBase;
	
	@PostConstruct
	private void initKieBase() {
		KieServices kieService = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieService.newKieFileSystem();
		
		//
		//	Load rules
		//
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			String rulesLocation = "classpath:/rules/*.drl";
			
			log.info("[RuleManagerImpl::initKieBase] Loading *.drl files from " + rulesLocation);
			org.springframework.core.io.Resource[] resources = resolver.getResources(rulesLocation);
			StringBuffer sb = new StringBuffer();
			int count = 0;
			for (org.springframework.core.io.Resource resource : resources) {
				FileInputStream fis = new FileInputStream(resource.getFile());
				org.kie.api.io.Resource rule = ResourceFactory.newInputStreamResource(fis);
				rule.setResourceType(ResourceType.DRL);
				
				sb.append("Loaded rule " + resource.getFilename() + "\r\n");
				kieFileSystem.write("/src/main/resources/rules/" + resource.getFilename(), rule);
				
				count++;
			}
			log.info("Loaded " + count + " rule(s):\r\n" + sb.toString());
		} catch (IOException e) {
			log.error("Problems in loading rules: " + e.getMessage(), e);
			throw new IllegalStateException(e);
		}

		//
		//	Compile rules
		//
		KieBuilder kieBuilder = kieService.newKieBuilder(kieFileSystem).buildAll();
		if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
			StringBuffer sb = new StringBuffer();
			for (Message result : kieBuilder.getResults().getMessages()) {
				sb.append(result.getText() + "\r\n");
				log.error(result.getText());
			}
			throw new IllegalStateException("Compilation error(s) in Drools rules: " + sb);
		}
		
		//
		//	Create Kie base
		//
		KieContainer kieContainer = kieService.newKieContainer(kieService.getRepository().getDefaultReleaseId());
		KieBaseConfiguration kbaseConf = kieService.newKieBaseConfiguration();
		kieBase = kieContainer.newKieBase(kbaseConf);
	}

	public StatelessKieSession getKieSession() {
		if (kieBase == null) {
			throw new IllegalStateException("KIE base not initialised");
		}
		
		return kieBase.newStatelessKieSession();
	}
	

}
