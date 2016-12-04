package de.wps.dot.infrastructure;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.logging.Logger;

public class ConsulTestAgent {

	private static Logger logger = Logger.getLogger(ConsulTestAgent.class.getName());

	private Process consulProcess;

	public void start() throws InterruptedException {
		try {
			consulProcess = Runtime.getRuntime().exec("/bin/bash");
			readStream();
			PrintWriter out = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(consulProcess.getOutputStream())), true);
			out.println("consul agent -dev -bind 127.0.0.1");
		} catch (IOException e) {
			logger.info("Befehl 'consul' nicht gefunden. Versuche consul.exe...");
			try {
				consulProcess = Runtime.getRuntime().exec("../consul agent -dev -bind 127.0.0.1");
				readStream();
			} catch (IOException e2) {
				logger.severe("Consul kann nicht gestartet werden.");
				throw new RuntimeException(e2);
			}
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				stop();
			}
		}));
	}

	private void readStream() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				InputStream inputStream = consulProcess.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				try {
					while ((line = reader.readLine()) != null) {
						logger.info(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void stop() {
		if (consulProcess != null){
			consulProcess.destroy();
			consulProcess = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		stop();
		super.finalize();
	}
}
