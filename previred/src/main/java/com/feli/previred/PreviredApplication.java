package com.feli.previred;

import java.io.InputStreamReader;
import java.util.Scanner;

import com.google.gson.Gson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.feli.previred,", "controller", "service" })
public class PreviredApplication {

	static Gson v_gson = new Gson();
	
	public static void main(String[] args) {
		SpringApplication.run(PreviredApplication.class, args);
		Periodo periodo =  new Periodo();
		periodo = getJSON(periodo);
		ApiController controller = new ApiController();
        periodo = controller.formatearJSON(periodo);
		System.out.println(v_gson.toJson(periodo));
	}

	private static Periodo getJSON(Periodo periodo) {
		try {
			Scanner reader = new Scanner(new InputStreamReader(System.in));
			StringBuffer buffer = new StringBuffer();
			while (reader.hasNextLine()) {
				buffer.append(reader.nextLine());
			}
			periodo = v_gson.fromJson(buffer.toString(), Periodo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return periodo;
	}

}
