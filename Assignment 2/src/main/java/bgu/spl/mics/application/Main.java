package bgu.spl.mics.application;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Input;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {
	public static void main(String[] args) {
		Gson g = new GsonBuilder().setPrettyPrinting().create();
		Reader r = null;
		try {
			r = new FileReader(args[0]);
			Input input = g.fromJson(r, Input.class);

			MicroService leiaService = new LeiaMicroservice(input.getAttacks());
			MicroService hansoloService = new HanSoloMicroservice();
			MicroService c3poService = new C3POMicroservice();
			MicroService r2d2Service = new R2D2Microservice(input.getR2D2());
			MicroService landoService = new LandoMicroservice(input.getLando());

			Ewoks ewoks = Ewoks.getInstance();
			ewoks.makeResource(input.getEwoks());

			Thread leia = new Thread(leiaService);
			Thread hansolo = new Thread(hansoloService);
			Thread c3po = new Thread(c3poService);
			Thread r2d2 = new Thread(r2d2Service);
			Thread lando = new Thread(landoService);

			hansolo.start();
			c3po.start();
			r2d2.start();
			lando.start();
			leia.start();

			try{//terminate gracefully
				r2d2.join();
				hansolo.join();
				lando.join();
				c3po.join();
				leia.join();
			}catch (InterruptedException ex){ex.printStackTrace();}

		} catch (Exception ex) {ex.printStackTrace();}

		Diary diary = Diary.getInstance();

		try {
			FileWriter writer = new FileWriter(args[1]);
			g.toJson(diary, writer);
			writer.flush();
			writer.close();
		} catch (IOException ex) {ex.printStackTrace();}
	}

}
















