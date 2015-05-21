package it.dpascotto.drools.tictactoe;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@Service
public class TicTacToePlayer {
	
	StatelessKieSession playerX, playerO;

	static Logger log = Logger.getLogger(TicTacToePlayer.class);

	public TicTacToePlayer() {
		playerX = createSession("classpath:/rules/set-X/*.drl", TicTacToeGame.X);
		playerO = createSession("classpath:/rules/set-O/*.drl", TicTacToeGame.O);
	}

	public List<TicTacToeGame> runStatistic(int numPlays) {
		if (!_isEven(numPlays)) {
			throw new IllegalArgumentException("Statistic must be made on even number of experiments");
		}
		List<TicTacToeGame> stat = new ArrayList<TicTacToeGame>();
		
		//
		//	Start alternativaely with X and O (for this reason
		//	we impose an even number of experiments
		//
		for (int i = 0; i < numPlays; i++) {
			stat.add(playAGame(_isEven(i) ? TicTacToeGame.X : TicTacToeGame.O));
		}
		
		viewStat(stat);
		return stat;
	}
	
	private boolean _isEven(int numPlays) {
		return (numPlays % 2 == 0);
	}

	public void viewStat(List<TicTacToeGame> stat) {
		int winX = 0, winO = 0, draw = 0;
		int tot = stat.size();
		for (TicTacToeGame ttt : stat) {
			if (ttt.status.equals(TicTacToeGame.X_WINS)) {
				winX++;
			} else if (ttt.status.equals(TicTacToeGame.O_WINS)) {
				winO++;
			} else if (ttt.status.equals(TicTacToeGame.DRAW)) {
				draw++;
			}
		}
		int X_100 = _perc(winX, tot);
		int O_100 = _perc(winO, tot);
		int D_100 = _perc(draw, tot);
		
		System.out.println(tot + " games, X wins " + X_100 + "%, O wins " + O_100 + "%, draw " + D_100 + "%");
	}

	private int _perc(int x, int tot) {
		double d = ((double)x / (double)tot) * 100;
		int perc = (int)Math.round(d); 
		
		return perc;
	}

	public TicTacToeGame playAGame(String playsFirst) {
		TicTacToeGame ttt = new TicTacToeGame(playsFirst);
		
		int count = 0;
		while (!ttt.gameIsFinished() && count < 20) {
			count++;
			if (ttt.isUpTo(TicTacToeGame.X)) {
				playerX.execute(ttt);
			} else {
				playerO.execute(ttt);
			}
		}
		
		log.debug("Match terminated (winner = " + (ttt.getWinner() != null ? ttt.getWinner() : "none") + ", number of moves = " + ttt.moveCount + ")");
		log.debug("\r\n" + ttt);
		
		return ttt;
	}
	
	
	public static StatelessKieSession createSession(String rulesLocation, String player) {
		KieServices kieService = KieServices.Factory.get();
		KieFileSystem kieFileSystem = kieService.newKieFileSystem();
		
		//
		//	Load rules
		//
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		try {
			
			log.info("[TicTacToePlayer::initKieBase] Loading *.drl files from " + rulesLocation);
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
		KieBase kieBase = kieContainer.newKieBase(kbaseConf);
		
		//
		//	Create session
		//
		StatelessKieSession kieSession = kieBase.newStatelessKieSession();
		
		kieSession.setGlobal("log", log);
		kieSession.setGlobal("player", player);
		
		return kieSession;

	}


}
