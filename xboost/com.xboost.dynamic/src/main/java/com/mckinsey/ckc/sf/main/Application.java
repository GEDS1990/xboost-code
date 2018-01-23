package com.mckinsey.ckc.sf.main;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mckinsey.ckc.sf.restful.data.Greeting;
import com.mckinsey.ckc.sf.restful.data.MoveRequest;
import com.mckinsey.ckc.sf.restful.data.MoveResponse;

@RestController
@EnableAutoConfiguration
public class Application {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/")
	public ResponseEntity<MoveResponse> get() {

		MoveResponse car = new MoveResponse();
		car.setNextLat(31.78);
		car.setNextLong(131.344);
		car.setSpendTime(23);
//		List<Parcel> parcels = new ArrayList<Parcel>();

		return new ResponseEntity<MoveResponse>(car, HttpStatus.OK);
	}

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@RequestMapping(value = "/move", method = RequestMethod.POST)
	public ResponseEntity<MoveResponse> move(@RequestBody MoveRequest request) {
		MoveResponse response = new MoveResponse();
		if (request != null) {
			int timeID = request.getTimeID();
			System.out.println("time id :"+timeID);
			int carrierId = request.getCarrierID();
			System.out.println("carrier id :"+carrierId);
			if( Main.responseList.get(timeID) != null)
				response = Main.responseList.get(timeID).get(carrierId);
		}

		// TODO: call persistence layer to update
		return new ResponseEntity<MoveResponse>(response, HttpStatus.OK);
	}

	public static void main(String[] args) throws Exception {

//		double A2[][] = new double[96820][9682];
//		double A12[] = new double[1200000];
//		for (int iw = 0; iw < 12000; iw++) {
//			for (int jw = 0; jw < 12000; jw++){
//				A2[iw][jw] = 1;
//			}
//		}
//		Main main = new Main();
//		main.calculate();
		SpringApplication.run(Application.class, args);
	}
}