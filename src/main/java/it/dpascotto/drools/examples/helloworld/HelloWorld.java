package it.dpascotto.drools.examples.helloworld;

import java.util.Collection;

import org.kie.api.io.ResourceType;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.KnowledgeBase;
import org.kie.internal.KnowledgeBaseFactory;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.definition.KnowledgePackage;
import org.kie.internal.io.ResourceFactory;
import org.kie.internal.runtime.StatefulKnowledgeSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service ("hello-world")
public class HelloWorld {
	
	public StatefulKnowledgeSession getStatefulSession() {
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newClassPathResource("/rules/hello-world.drl", HelloWorld.class), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
		    System.out.println(kbuilder.getErrors().toString());
		    throw new RuntimeException("Unable to compile \"hello-world.drl\".");
		}

		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();

		kbase.addKnowledgePackages(pkgs);

		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		return ksession;
	}
	

	public void sayHello(String msg) {
		StatefulKnowledgeSession ksession = getStatefulSession();
		
		Message message = new Message();
		message.setMessage(msg);
		message.setStatus(Message.HELLO);

		ksession.insert(message);
		
		ksession.fireAllRules();

		//ksession.dispose();   
	}
}
