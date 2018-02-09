package com.mckinsey.ckc.sf.main;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import com.mckinsey.ckc.sf.data.Result;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
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
@EnableScheduling
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

	@RequestMapping(value = "/move",method = RequestMethod.GET)
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

	@RequestMapping(value="/main/calculate")
	public String calculate() {
		Main main = new Main();
		//main.calculate();
		main.start();
		return "success";
	}

	@RequestMapping(value="/mainhub/calculate")
	public String calculateHub() {
		Main_Hub mainHub = new Main_Hub();
		//main.calculate();
		mainHub.start();
		return "success";
	}
//
//	@RequestMapping(value="/main/calculateTimer")
//	public void calculateTimer(){
//		new Timer().schedule(new TimerTask() {
//			@Override
//			public void run() {
//				System.out.println(new Date());
//				System.out.println(Calendar.getInstance().getTime());
//				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：开始运行算法。。。。。");
//				//	Main main =new Main();
//				//	main.calculate();
//				System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：算法运行结束！");
//			}
//		},3000,10*1000);
//	}

	@Scheduled(cron="0 0 1 * * ?") //每天1点执行
	public void calculateTimer1() {
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：开始运行算法。。。。。");
		Main main =new Main();
		main.calculate();
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"：算法运行结束！");
	}

	@RequestMapping(value="/main/dynamic")
	public Map<String,Object> getCalculateResult(Integer timeId) {
		Map<String,Object> result=Main.query(timeId);
		return result;
	}


	@RequestMapping("/main")
	public List<Map<String,Object>> getMainResult() {
		//	Main main = new Main();
		//	List<Map<String,Object>> result=Main.main();
		List<Map<String,Object>> result=Main.main();
		return result;
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