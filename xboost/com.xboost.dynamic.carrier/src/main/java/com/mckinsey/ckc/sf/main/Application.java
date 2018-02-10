//package com.mckinsey.ckc.sf.main;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.atomic.AtomicLong;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.mckinsey.ckc.sf.constants.IConstants;
//import com.mckinsey.ckc.sf.data.CarrierRecords;
//import com.mckinsey.ckc.sf.data.Parcel;
//import com.mckinsey.ckc.sf.restful.data.DataVModel;
//import com.mckinsey.ckc.sf.restful.data.FeiXianModel;
//import com.mckinsey.ckc.sf.restful.data.Greeting;
//import com.mckinsey.ckc.sf.restful.data.MoveRequest;
//import com.mckinsey.ckc.sf.restful.data.MoveResponse;
//import com.mckinsey.ckc.sf.restful.data.XYModel;
//
//@RestController
//@EnableAutoConfiguration
//public class Application implements Filter,IConstants{
//
//	private static final String template = "Hello, %s!";
//	private final AtomicLong counter = new AtomicLong(120);
//	private static int timeID = 120;
//	
//
//	@RequestMapping("/")
//	public ResponseEntity<MoveResponse> get() {
//
//		MoveResponse car = new MoveResponse();
//		car.setNextLat(31.78);
//		car.setNextLong(131.344);
//		car.setSpendTime(23);
////		List<Parcel> parcels = new ArrayList<Parcel>();
//
//		return new ResponseEntity<MoveResponse>(car, HttpStatus.OK);
//	}
//
//	@RequestMapping("/greeting")
//	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
//		return new Greeting(counter.incrementAndGet(), String.format(template, name));
//	}
//
//	@RequestMapping(value = "/move", method = RequestMethod.POST)
//	public ResponseEntity<MoveResponse> move(@RequestBody MoveRequest request) {
//		MoveResponse response = new MoveResponse();
//		if (request != null) {
//			int timeID = request.getTimeID();
//			System.out.println("time id :"+timeID);
//			int carrierId = request.getCarrierID();
//			System.out.println("carrier id :"+carrierId);
//			if( Main.responseList.get(timeID) != null)
//				response = Main.responseList.get(timeID).get(carrierId);
//		}
//
//		// TODO: call persistence layer to update
//		return new ResponseEntity<MoveResponse>(response, HttpStatus.OK);
//	}
//	
//	
//	@RequestMapping("/davav")
//	public List<CarrierRecords> datav(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		timeID = (int)counter.incrementAndGet();
//		if(timeID > 288){
//			timeID = 120;
//			counter.set(120);
//		}
//		return Main.dataVMap.get(timeID);
//	}
//	
//	@RequestMapping("/date")
//	public List<XYModel> date(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		List<XYModel> dvList = new ArrayList<XYModel>();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		XYModel model = new XYModel();
//		Date date = new Date(System.currentTimeMillis());
//		date.setDate(17);
//		date.setMonth(4);
//		date.setYear(2017);
//		date.setHours(timeID*TIME_UNIT/60);
//		date.setMinutes(timeID*TIME_UNIT%60);
//		date.setSeconds(0);
//		model.setX(sdf.format(date));
//		dvList.add(model);
//		return dvList;
//	}
//	
//	
//	@RequestMapping("/davavstatistic")
//	public List<DataVModel> datav_statistic(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		DataVModel dv = Main.dataVStaticsticMap.get(timeID);
//		List<DataVModel> dvList = new ArrayList<DataVModel>();
//		dvList.add(dv);
//		return dvList;
//	}
//	
//	@RequestMapping("/carrier")
//	public List<XYModel> carrier(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		XYModel dv = new XYModel();
//		List<XYModel> dvList = new ArrayList<XYModel>();
//		dv.setX("普通骑士");
//		dv.setY(N_CARRIER_RANGE);
//		dvList.add(dv);
//		XYModel dv2 = new XYModel();
//		dv2.setX("扫尾骑士");
//		dv2.setY(N_SWOT_CARRIER);
//		dvList.add(dv2);
//		return dvList;
//	}
//	
//	@RequestMapping("/parcel_feixian")
//	public List<FeiXianModel> parcel_feixian(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		
//		List<FeiXianModel> dvList = new ArrayList<FeiXianModel>();
//		if(Main.timeIdParcelList.containsKey(timeID)){
//			for(Parcel p :Main.timeIdParcelList.get(timeID)){
//				FeiXianModel dv = new FeiXianModel();
//				dv.setX(p.getOrigPos().getX()+","+p.getOrigPos().getY());
//				dv.setY(p.getDestPos().getX()+","+p.getDestPos().getY());
//				dvList.add(dv);
//			}
//		}
//		
//		return dvList;
//	}
//	
//	@RequestMapping("/one_hour")
//	public List<XYModel> one_hour(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		List<XYModel> dvList = new ArrayList<XYModel>();
//		int index = timeID;
//		if(timeID < 132){
//			index = 120;
//		}else{
//			index = timeID-12;
//		}
//		int sum = 0;
//		for(;index <= timeID;index++){
//			XYModel dv = new XYModel();
//			if(Main.timeIdParcel.containsKey(index)){
//				Date date = new Date(System.currentTimeMillis());
//				date.setHours(index*TIME_UNIT/60);
//				date.setMinutes(index*TIME_UNIT%60);
//				date.setSeconds(0);
//				dv.setX(sdf.format(date));
//				if(index <= 120 || index >= 216){
//					dv.setY(0);
//				}else{
//					dv.setY(Main.timeIdParcel.get(index));
//				}
//				dvList.add(dv);
//			}
//		}
//		return dvList;
//	}
//	
//	
//	@RequestMapping("/pressure")
//	public List<XYModel> pressure(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//		List<XYModel> dvList = new ArrayList<XYModel>();
//		int index = timeID;
//		if(timeID < 132){
//			index = 120;
//		}else{
//			index = timeID-12;
//		}
//		for(;index <= timeID;index++){
//			XYModel dv = new XYModel();
//			if(Main.timeIdPressure.containsKey(index)){
//				Date date = new Date(System.currentTimeMillis());
//				date.setHours(index*TIME_UNIT/60);
//				date.setMinutes(index*TIME_UNIT%60);
//				date.setSeconds(0);
//				dv.setX(sdf.format(date));
//				dv.setY(Main.timeIdPressure.get(index));
//				dvList.add(dv);
//			}
//		}
//		return dvList;
//	}
//	
//	
//	@RequestMapping("/realtime_parcel")
//	public List<XYModel> realtime_parcel(@RequestParam(value = "carrierid", defaultValue = "World") String name) {
//		XYModel dv = new XYModel();
//		List<XYModel> dvList = new ArrayList<XYModel>();
//		dv.setX("未处理快件");
//		dv.setY(Main.dataVStaticsticMap.get(timeID).getUnattended());
//		dvList.add(dv);
//		XYModel dv2 = new XYModel();
//		dv2.setX("已取快件");
//		dv2.setY(Main.dataVStaticsticMap.get(timeID).getCurrentDelivery());
//		dvList.add(dv2);
//		XYModel dv3 = new XYModel();
//		dv3.setX("已存快件");
//		dv3.setY(Main.dataVStaticsticMap.get(timeID).getPickup());
//		dvList.add(dv3);
//		return dvList;
//	}
//
//	public static void main(String[] args) throws Exception {
//		Main main = new Main();
//		main.calculate();
//		SpringApplication.run(Application.class, args);
//	}
//
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
//        chain.doFilter(req, res);
//    }
//
//    public void init(FilterConfig filterConfig) {}
//
//    public void destroy() {}
//
//}