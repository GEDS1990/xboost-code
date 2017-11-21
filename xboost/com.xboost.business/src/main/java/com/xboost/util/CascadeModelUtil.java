package com.xboost.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mckinsey.sf.constants.IConstants;
import com.mckinsey.sf.data.*;
import com.mckinsey.sf.data.car.DefaultInfiniteCarManager;
import com.mckinsey.sf.data.car.ICarManager;
import com.mckinsey.sf.data.config.*;
import com.mckinsey.sf.data.config.NoiseMaker;
import com.mckinsey.sf.data.constraint.DefaultConstraints;
import com.mckinsey.sf.data.constraint.IConstraint;
import com.mckinsey.sf.data.solution.Solution;
import com.mckinsey.sf.data.solution.SolutionJson;
import com.mckinsey.sf.insertion.GreedyInsertion;
import com.mckinsey.sf.insertion.IInsertion;
import com.mckinsey.sf.insertion.RegretInsertion;
import com.mckinsey.sf.palns.PALNS;
import com.mckinsey.sf.printer.OutputPrinter;
import com.mckinsey.sf.removal.*;
import com.mckinsey.sf.utils.ExcelToJson;
import com.xboost.pojo.Configuration;
import com.xboost.service.DemandInfoService;
import com.xboost.service.SiteDistService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10 0010.
 * 串点模型工具类
 */
public class CascadeModelUtil implements IConstants {
    private  PalnsConfig palnsConf;
    private  TransportCost transportCost;
    private  ICostCalculator costCalculator;
    private  TimeConstraint timeConstraint;
    private  DistanceConstraint distanceConstraint;
    private  RandomRemoval randomRemoval;
    private  RouteRemoval routeRemoval;
    private  WorstRemoval worstRemoval;
    private  ShawRemoval shawRemoval;
    private  RegretInsertion regretInsertion;
    private  NoiseMaker noiseMaker;
    private  JobPacker jobPacker;
    public static Map<String,Job> totalJobs = new HashMap<String,Job>();
    public static Map<String,Location> locs = new HashMap<String,Location>();
    public static HashMap<String,Integer> carsMap = new HashMap<String,Integer>();
    public static int distMode;

    public void excute(Configuration config,DemandInfoService demandInfoService,SiteDistService siteDistService){
        String inputJsonPath = "src/main/resources/input.json";
        String demandFilePath = "src/main/resources/demand/";
//        Input inputJson = ExcelToJson.transferInput();
        //initialize input and config from json file and constants
        Config conf = initConf(config,demandInfoService);

        Input input = initInput(config , demandInfoService);
        long startTime = System.currentTimeMillis();
        OutputPrinter.printLine("s	tart init ...");
        RoutingTransportCosts transportCost = new RoutingTransportCosts(siteDistService, conf.getTransportCost().getDistance(), conf.getTransportCost().getNearest(), conf.getTransportCost().getFixed_stop_time(), false,distMode);
        DefaultConstraints defaultCons = new DefaultConstraints(conf.getDistanceConstraint().getWeight(),conf.getTimeConstraint().getWeight(),1,transportCost);
        IConstraint[] cons = {defaultCons};

        ICarManager cm = new DefaultInfiniteCarManager(input.getCarTemplates(),transportCost);
//		DefaultCostCalculator obj = new DefaultCostCalculator(conf.getCostCalculator().getMin(),conf.getCostCalculator().getMax(),conf.getCostCalculator().getN());
        NoiseMaker noiser = conf.getNoiseMaker();
        noiser.setMaxNoise(transportCost.getMaxDistance()*conf.getDistanceConstraint().getWeight());
        DefaultJobPacker jobPacker = new DefaultJobPacker(conf.getJobPacker().getInterval(),1);

        OutputPrinter.printLine("start packing....");
        OutputPrinter.printLine("before packing: "+ input.getInitSolution().getUnassignedJobs().length);
        double volume = 0;
        //从数据库中读数据
        for(Job j : input.getInitSolution().getUnassignedJobs()){
            volume += j.getDimensions()[0];
        }
        OutputPrinter.printLine("total volume: "+ volume);

        SolutionJson initSolutionBeforePack = jobPacker.pack(input.getInitSolution());
        OutputPrinter.printLine("initSolution:"+initSolutionBeforePack.getRoutes().length+","+initSolutionBeforePack.getUnassignedJobs().length);
        OutputPrinter.printLine("build initSolution....");

        GreedyInsertion constructive = new GreedyInsertion();
        constructive.setTransportCost(transportCost);

        Solution initSolutionAfterPack = Solution.newSolution(initSolutionBeforePack.getUnassignedJobs(),initSolutionBeforePack.getRoutes(),
                cons,cm,costCalculator,noiser,constructive);

        OutputPrinter.printLine("initSolution:"+initSolutionAfterPack.getRoutes().size()+" "+initSolutionAfterPack.getUnassigned().size());
        for(Job j: initSolutionAfterPack.getUnassigned().values()){
            System.out.println(j.getId());
        }
        List<IRemoval> rops = new ArrayList<IRemoval>();
        //add shawRemoval
        shawRemoval.setTransportCost(transportCost);
        rops.add(shawRemoval);
        //add random removal
        randomRemoval.setTransportCost(transportCost);
        rops.add(randomRemoval);
        worstRemoval.setTransportCost(transportCost);
        rops.add(worstRemoval);
        routeRemoval.setTransportCost(transportCost);
        rops.add(routeRemoval);
        //insertion list
        List<IInsertion> iops = new ArrayList<IInsertion>();
        //add greedy insertion
        GreedyInsertion gi = new GreedyInsertion();
        gi.setTransportCost(transportCost);
        iops.add(gi);
        //add regret insertion
        for(int k : regretInsertion.getK()){
            iops.add(new RegretInsertion(k,transportCost));
        }
        PALNS algo = new PALNS(initSolutionAfterPack, rops, iops, conf.getPconf());
        OutputPrinter.printLine("start running ...");
        Solution finalPackSolution = (Solution)algo.runAlgo();
//		OutputPrinter.PrintSolution(finalPackSolution);
//		OutputPrinter.PrintProblem(finalPackSolution);
//		OutputPrinter.PrintUnassigned(finalPackSolution);
        SolutionJson finalSolutionJson = finalPackSolution.SolutionToJson(finalPackSolution);
        finalSolutionJson = jobPacker.unpack(finalSolutionJson);
        //将finalSolutionJson存储到数据库

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("src/main/resources/solution/solution.json"), finalSolutionJson);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Solution finalSolution = Solution.newSolution(finalSolutionJson.getUnassignedJobs(),finalSolutionJson.getRoutes(),
                cons,cm,costCalculator,noiser,constructive);
        OutputPrinter.PrintSolution(finalSolution);
        OutputPrinter.PrintProblem(finalSolution);
        OutputPrinter.PrintUnassigned(finalSolution);

        //calculate max stops
        OutputPrinter.PrintRoutes(finalSolution);

        OutputPrinter.printLine("finished.");
        long endTime = System.currentTimeMillis();
        OutputPrinter.printLine("total time used:"+(endTime-startTime)+"ms");

//		OutputPrinter.writeSolutionToExcel(finalSolution);
        OutputPrinter.writeStandardOutputToExcel(finalSolution,transportCost);
    }
    private Config initConf(Configuration config,DemandInfoService demandInfoService) {

        distMode = config.getDistMode();
        Score[] scores = new Score[]{Score.REJECTED,Score.ACCEPTED,Score.BETTER_THAN_CURRENT,Score.NEWBEST};
        palnsConf = new PalnsConfig(PALNS_CORES, config.getOptimizeIterations(),PALNS_MAX_TIME, PALNS_W,PALNS_DECAY, PALNS_ALPHA, scores,
                PALNS_SEGMENT);
//        transportCost = new TransportCost("src/main/resources/distance/"+config.getDistanceFile(),TRANSPORT_COST_NEAREST,config.getLoadTime());
        //读取文件改为读取数据库（替换BufferedReader）
        transportCost = new TransportCost("",TRANSPORT_COST_NEAREST,config.getLoadTime());



        costCalculator = new DefaultCostCalculator(COST_CALCULATOR_MIN,COST_CALCULATOR_MAX,COST_CALCULATOR_N);
        timeConstraint = new TimeConstraint(TIME_CONSTRAINT_WEIGHT);
        distanceConstraint = new DistanceConstraint(DISTANCE_CONSTRAINT_WEIGHT);
        randomRemoval = new RandomRemoval(RANDOM_REMOVAL_K,RANDOM_REMOVAL_MAX_K);
        routeRemoval = new RouteRemoval(ROUTE_REMOVAL_K);
        worstRemoval = new WorstRemoval(WORST_REMOVAL_K,WORST_REMOVAL_P,WORST_REMOVAL_MAX_K);
        shawRemoval = new ShawRemoval(SHAW_REMOVAL_WS, SHAW_REMOVAL_WT, SHAW_REMOVAL_WD, SHAW_REMOVAL_WC,SHAW_REMOVAL_P, SHAW_REMOVAL_K,SHAW_REMOVAL_MAX_K,0);
        regretInsertion = new RegretInsertion(REGRET_INSERTION_K);
        noiseMaker = new NoiseMaker(NOISE_LEVEL,NOISE_PORB);
        jobPacker = new JobPacker(JOB_PACKER_INTERVAL);

        Config conf = new Config(palnsConf,transportCost, costCalculator,
                timeConstraint, distanceConstraint, randomRemoval,
                routeRemoval, worstRemoval, shawRemoval,
                regretInsertion, noiseMaker, jobPacker);
        return conf;
    }
    private Input initInput(Configuration config,DemandInfoService demandInfoService) {
        Input input = null;
//            input = mapper.readValue(new File("src/main/resources/progInput.json"), Input.class);
        ExcelToJson etj = new ExcelToJson();
            input = etj.transferInput(config,demandInfoService);//替换掉对progInput.json的依赖
            for(Job j: input.getInitSolution().getUnassignedJobs()){
                totalJobs.put(j.getId(), j);
            }
            for(Car c:input.getCarTemplates()){
                carsMap.put(c.getType(), 0);
            }
        return input;
    }

}
