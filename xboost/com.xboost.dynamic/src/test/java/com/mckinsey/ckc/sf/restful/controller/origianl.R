  ##########################################################################
  ##################Update Log##############################################
  ###Eric Fan @ 2016/12/22 3:22pm###########################################
  ###code first draft#######################################################
  ##########################################################################
  ###Eric Fan @ 2016/12/28 11:52am##########################################
  ###code version two#######################################################
  ###stage 1 model: stage 1 assumptions
  ##########################################################################
  ###Eric Fan @ 2016/12/28 7:00pm###########################################
  ###code version two#######################################################
  ###stage 1.5 model: stage 1 assumptions+angle calculation#################
  ##########################################################################
  ##########################################################################
  ###Eric Fan @ 2016/12/28 7:30pm###########################################
  ###code version two#######################################################
  ###stage 2 model: stage 2 assumptions+angle calculation+logic updated#####
  ##########################################################################
  ###Eric Fan @ 2016/12/29 10:46am##########################################
  ###code version two#######################################################
  ###stage 2 model: stage 2 refreshed data to demand.v3 with middle ring####
  ##########################################################################
  ###Frank Lai @ 2016/12/29 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: add "pooling without reroute" 
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: fix some bugs on opportunity data
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: update decision logic
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: upgrade delivery priority
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: add reroute decision by angle and distance
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: add swot team
  ##########################################################################
  ###Frank Lai @ 2016/3/1 10:20pm##########################################
  ###code version two#######################################################
  ###stage 2 model: upgrade swot logic
  ##########################################################################
  
  ###load environment
  Sys.setlocale("LC_ALL","Chinese")
  require(xlsx)
  require(sqldf)
  require(Matrix)
  require(ggmap)
  require(ggplot2)
  require(ggrepel)
  require(dplyr)
  require(lubridate)
  require(tspmeta)
  require(TSP)
  library(data.table)
  model_version <- "v8.2"
  source(paste("~/Shunfeng/dynamic network_function_definition_v8.1.R",sep=""))
  
  # ninepoints <- read.xlsx("9个点.xlsx",sheetIndex=1,encoding="UTF-8",colClasses=c("numeric","numeric","integer"),header=T)
  # coords.df <- data.frame(long=ninepoints$lon,lat=ninepoints$lat)
  # coords.df <- coords.df[1:2,]
  # for(i in 1:500){
  #   num_parcel <- nrow(coords.df)
  #   coords.mx <- as.matrix(coords.df)
  #   dist.mx <- dist(coords.mx)
  #   # dist.mx <- dist(coords.mx)
  #   #tsp.ins <- tsp_instance(coords.mx, dist.mx )
  #   # tour <- run_solver(tsp.ins, method="2-opt")
  #   tour <- solve_TSP(TSP(dist.mx), method="nearest_insertion",start=1L)
  #   end_p <- tour[[num_parcel]]
  #   tour_length(tour)
  #   autoplot(tsp.ins, tour)
  # }
  # num_parcel <- nrow(carrier@parcel_list)
  # coords.mx <- as.matrix(carrier@parcel_list)
  # dist.mx <- dist(coords.mx)
  # # dist.mx <- dist(coords.mx)
  # # tsp.ins <- tsp_instance(coords.mx, dist.mx )
  # # tour <- run_solver(tsp.ins, method="2-opt")
  # tour <- solve_TSP(TSP(dist.mx), method="nearest_insertion", start=1L)
  # end_p <- tour[[num_parcel]]
  # total_shipping_time <- tour_length(tour)-dist.mx[[((end_p-2)*(end_p-1)/2+1)]]
  
  
  
  
  
  ###Parameter Assumptions
  carrier_supply <- 9999 #unlimited carrier resource
  carrier_capacity <- 20 #unlimited carrier capacity
  # carrier_speed_kmh <- 20 #km/h
  # carrier_speed_mm <- carrier_speed_kmh*1000/60 #m/minute
  time_unit <- 5 #time unit in minutes
  pickup_time <- 0 #time needed to pick up a package
  dropoff_time <- 0 #time needed to drop off a package
  picking_start_time <- 10
  picking_end_time <- 24
  demand_cut_time <- 18
  simu_hour <- 8
  simu_time_unit <- simu_hour*60/time_unit
  distance_inflator <- 100*1000
  #interest_radius<-5000
  interest_rank <- 10
  max_angle0 <- 60
  required_shipping_time <- 3
  #how many hours before cut time should we loose the angle in decistion function
  open_timing <- 2
  opt_importance_range <- seq(0,0,1)
  N_carrier_range <- 50
  speed_range <- seq(20,20,5)
  opt_max_angle<-45
  #parameters for think method
  para_a <- 2 #importance for opportunity demand against currently shipping demand
  para_b <- 2   #importance for current location closeness against destination closeness
  para_c <- 1000    #factor to compare remaining time against distance 
  #swot capacity
  swot_capacity<-3
  urgency_lead_time<-1.5
  N_swot_carrier <- 50
  picking_start_time_swot <- urgency_lead_time+picking_start_time
  swot_end_time<-20
  ######################################################################################
  ###data processing####################################################################
  ######################################################################################
  
  ###data complete rate
  data_complete_rate <- 0.7755929
  
  #read in demand data
  demand3<-read.csv("~/20170306_SF/dynamic network/demand_data_12292016_v3.csv",stringsAsFactors = FALSE,nrows = 200)
  demand3 <- demand3[demand3$inbound_depot_id!=demand3$outbound_depot_id,]
  #read in jiebo data
  jiebo_selected<-read.csv("~/20170306_SF/dynamic network/jieboselected_12292016_v1.csv",stringsAsFactors = FALSE)
  #merge to get inbound and outbound jiebo coordinates
  demand3$inbound_lon<-jiebo_selected$candidate_lon[demand3$inbound_depot_id]
  demand3$inbound_lat<-jiebo_selected$candidate_lat[demand3$inbound_depot_id]
  demand3$outbound_lon<-jiebo_selected$candidate_lon[demand3$outbound_depot_id]
  demand3$outbound_lat<-jiebo_selected$candidate_lat[demand3$outbound_depot_id]
  columns<-c("寄件时间","派件时间","inbound_depot_id","outbound_depot_id",
             "inbound_lon","inbound_lat","outbound_lon","outbound_lat")
  demand3<-demand3[,columns]
  #get date
  demand3$date<-date(as.POSIXct(demand3$寄件时间))
  #get hours
  demand3$hour<-hour(as.POSIXct(demand3$寄件时间))
  #round actual minute to the nearest time unit
  demand3 <- demand3[demand3$hour<demand_cut_time,]
  demand3$minute <- floor(minute(as.POSIXct(demand3$寄件时间))/time_unit)*time_unit
  #simulatied time
  demand3<-filter(demand3,date=="2016-10-25")
  demand3$time_simu<-demand3$hour+demand3$minute/60
  demand3$time_id<-demand3$hour*60/time_unit+demand3$minute/time_unit
  #write.csv(demand3,"~/20170306_SF/dynamic network/demand_data_12292016_v3_after_cleanse.csv")
  
  begin_time_total <- proc.time()
  #hist(as.POSIXct(demand2$寄件时间),breaks=168)
  
  #aggregate demand group by time and OD
  OD_demand<-sqldf("select time_id,
                   inbound_depot_id as inbound_id,
                   inbound_lon as inbound_lon,
                   inbound_lat as inbound_lat,
                   outbound_depot_id as outbound_id,
                   outbound_lon as outbound_lon,
                   outbound_lat as outbound_lat,
                   count(*) as demand_cnt
                   from demand3
                   group by 1,2,3,4,5,6,7")
  ###get inner ring ODs
  OD_demand$daily_volume<-OD_demand$demand_cnt/data_complete_rate
  OD_demand$demand_id<-1:nrow(OD_demand)
  OD_demand<-filter(OD_demand,(time_id<=18*60/time_unit))
  OD_demand$time_id[OD_demand$time_id<=picking_start_time*60/time_unit]<-picking_start_time*60/time_unit
  sum(OD_demand$daily_volume)
  #write.csv(OD_demand,"~/20170306_SF/dynamic network/on_demand.csv")
  #Point list
  points<-sqldf("select inbound_id as point_id,
                inbound_lon as point_lon,
                inbound_lat as point_lat,
                sum(daily_volume) as initial_volume
                from OD_demand
                group by 1,2,3")
  
  points$point_weight<-points$initial_volume/sum(points$initial_volume)
  write.csv(points,"~/20170306_SF/dynamic network/points.csv")
  
  ############################################################
  ###simulation part##########################################
  ############################################################
  
  result<-data.frame(     Speed=integer(),
                          opt_importance=double(),
                          N_carrier=integer(),
                          N_parcel=integer(),
                          N_parcel_delivered=integer(),
                          parcel_per_carrier=double(),
                          avg_working_hour=double(),
                          avg_actual_working_hour=double(),
                          avg_parcel_on_carrier=double(),
                          avg_shipping_time=double(),
                          on_time_rate=double(),
                          distance_per_carrier=double(),
                          total_distance=double())
  
  
  for(speed_i in speed_range){
  
    carrier_speed_kmh <- speed_i #km/h
    carrier_speed_mm <- carrier_speed_kmh*1000/60 #m/minute
  
    for(opt_importance in opt_importance_range) {
      for(N_carrier in N_carrier_range) {
        
        set.seed(50) 
       
        ###Create all time parcel reference table
        parcels_tbl_all<-sqldf("select demand_id as parcel_id,
                               inbound_lon as parcel_origin_lon,
                               inbound_lat as parcel_origin_lat,
                               0 as parcel_carrier_id,
                               inbound_lon as parcel_current_lon,
                               inbound_lat as parcel_current_lat,
                               outbound_lon as parcel_dest_lon,
                               outbound_lat as parcel_dest_lat,
                               daily_volume as parcel_volume,
                               time_id as parcel_time_id_initial,
                               0 as parcel_deliver_status
                               from OD_demand")
  
  
        parcels_tbl_all$parcel_deadline<-parcels_tbl_all$parcel_time_id_initial+60/time_unit*required_shipping_time
        parcels_tbl_all$parcel_deliver_time<-0
        parcels_tbl_all$parcel_pickup_time<-0
        #no parcels are urgent at first
        parcels_tbl_all$parcel_group_id <- 0
        # parcels_tbl_all$mintime_to_dest <- 0
        parcels_tbl_all$mintime_to_dest <- sqrt((parcels_tbl_all$parcel_origin_lon-parcels_tbl_all$parcel_dest_lon)^2+
                                                  (parcels_tbl_all$parcel_origin_lat-parcels_tbl_all$parcel_dest_lat)^2)*
          distance_inflator/carrier_speed_mm
  
        ###Create Carriers
        #create carrier reference table
  
        P<-nrow(points)
        N<-N_carrier
        N_swot <- N_swot_carrier
        #get carriers' initial positions
        initial_point_index<-floor(runif(N,min =1,max=P))
        carriers_tbl_all<-points[initial_point_index,c("point_lon","point_lat","point_lon","point_lat")]
        carriers_tbl_all$carrier_id<-1:N
        colnames(carriers_tbl_all)<-c("carrier_initial_lon","carrier_initial_lat",
                                      "carrier_current_lon","carrier_current_lat","carrier_id")
        carriers_tbl_all$carrier_dest_lon<-0
        carriers_tbl_all$carrier_dest_lat<-0
        carriers_tbl_all$group_id<-0
        carrier_records<-data.frame(time_id=integer(),
                                    carrier_id=integer(),
                                    group_id=integer(),
                                    carrier_current_lon=double(),
                                    carrier_current_lat=double(),
                                    carrier_dest_lon=double(),
                                    carrier_dest_lat=double(),
                                    distance_traveled=double(),
                                    parcel_type=integer(),
                                    parcel_volume=double(),
                                    pickup_cnt=integer(),
                                    dropoff_cnt=integer()
        )
        
        #initialize carriers
        carriers<-vector("list",N)
        for(n in 1:N) {
          carriers[[n]]<-carrier( ID=carriers_tbl_all$carrier_id[n],
                                  group_id = carriers_tbl_all$group_id[n],
                                  position_current=c(carriers_tbl_all$carrier_initial_lon[n],
                                                     carriers_tbl_all$carrier_initial_lat[n]),
                                  speed=carrier_speed_mm)
        }
  
  
        ###calculate parcels' theta1
        parcels_tbl_all$parcel_theta<-180/pi*acos((parcels_tbl_all$parcel_dest_lon-parcels_tbl_all$parcel_origin_lon)/sqrt((parcels_tbl_all$parcel_dest_lon-parcels_tbl_all$parcel_origin_lon)^2+
                                                                                                                             (parcels_tbl_all$parcel_dest_lat-parcels_tbl_all$parcel_origin_lat)^2))
        parcels_tbl_all$parcel_theta<-((parcels_tbl_all$parcel_dest_lat-parcels_tbl_all$parcel_origin_lat)>=0)*parcels_tbl_all$parcel_theta+
          ((parcels_tbl_all$parcel_dest_lat-parcels_tbl_all$parcel_origin_lat)<0)*(360-parcels_tbl_all$parcel_theta) 
  
        ###start running simulation
        #loop over time
        #for(t in (picking_start_time*60/time_unit):(24*60/time_unit)) {
        # for(t in (picking_start_time*60/time_unit):(picking_end_time*60/time_unit)) {
        
        for(t in (picking_start_time*60/time_unit):(picking_end_time*60/time_unit)) {  
  
          ###SWOT Team show up
          if (t == picking_start_time_swot*60/time_unit){
            initial_point_index<-floor(runif(N_swot,min =1,max=P))
            carriers_tbl_all_swot<-points[initial_point_index,c("point_lon","point_lat","point_lon","point_lat")]
            carriers_tbl_all_swot$carrier_id<-(N+1):(N+N_swot)
            colnames(carriers_tbl_all_swot)<-c("carrier_initial_lon","carrier_initial_lat",
                                               "carrier_current_lon","carrier_current_lat","carrier_id")
            carriers_tbl_all_swot$carrier_dest_lon<-carriers_tbl_all_swot$carrier_current_lon
            carriers_tbl_all_swot$carrier_dest_lat<-carriers_tbl_all_swot$carrier_current_lat
            carriers_tbl_all_swot$group_id<-1
          
            #carriers_tbl_all <- rbind(carriers_tbl_all,carriers_tbl_all_swot)
            
            carriers_tbl_all <- data.frame(rbindlist(list(carriers_tbl_all,carriers_tbl_all_swot)))
            
            
            #initialize swot carriers
            for(n in (N+1):(N+N_swot)) {
              carriers[[n]]<-carrier( ID=carriers_tbl_all$carrier_id[n],
                                      group_id = carriers_tbl_all$group_id[n],
                                      position_current=c(carriers_tbl_all$carrier_initial_lon[n],
                                                         carriers_tbl_all$carrier_initial_lat[n]),
                                      position_dest=c(carriers_tbl_all$carrier_dest_lon[n],
                                                      carriers_tbl_all$carrier_dest_lat[n]),
                                      speed=carrier_speed_mm)
            }
            N <- N+N_swot
          }
          ###swot end###
          if(t==swot_end_time*60/time_unit)
          {
            parcels_tbl_all$parcel_group_id<-0
            carriers_tbl_all$group_id<-0
            for(i1 in 1:N)
            {
              carriers[[i1]]@group_id<-0
            }
          }
  
          if(t>230){
            max_angle0=180
          }
          #max angle changes over time
          max_angle <- (t >= demand_cut_time*60/time_unit - open_timing*60/time_unit)*(180-max_angle0) + max_angle0
          #indentify urgent parcels
          parcels_tbl_all$parcel_group_id <- 
            (parcels_tbl_all$parcel_deliver_status==0)*(t-parcels_tbl_all$parcel_time_id_initial>=urgency_lead_time*(60/time_unit))*1*(t<swot_end_time*60/time_unit)
          #parcels occured before time t
          parcels_tbl_t<-filter(parcels_tbl_all,(parcel_deliver_status%in%c(0,-1))&(parcel_time_id_initial<=t))
          #only select opportunity with lowest distances
          opportunities<-merge(carriers_tbl_all,parcels_tbl_t,by=NULL)
          opportunities <- opportunities[opportunities$parcel_group_id==opportunities$group_id,]
          opportunities$current_distance<-sqrt((opportunities$carrier_current_lon-opportunities$parcel_current_lon)^2+
                                                 (opportunities$carrier_current_lat-opportunities$parcel_current_lat)^2)*
            distance_inflator
          #calculate current distance between carrier and parcel
          opportunities<-opportunities %>%
            group_by(carrier_id) %>%
            top_n(n=-interest_rank,wt=current_distance)
          
          #calculate destination distance between carrier and parcel
          opportunities$dest_distance<-sqrt((opportunities$carrier_dest_lon-opportunities$parcel_dest_lon)^2+
                                              (opportunities$carrier_dest_lat-opportunities$parcel_dest_lat)^2)*
            distance_inflator
          
          #calculate opporunity parcel's urgency
          opportunities$opt_parcel_time_remaining<-opportunities$parcel_deadline-t
          opportunities$opt_parcel_urgency<-exp(required_shipping_time-2-opt_importance-opportunities$opt_parcel_time_remaining)
          
          #parcel's OD distance
          opportunities$parcel_OD_distance<-apply(opportunities[,c("parcel_origin_lon","parcel_origin_lat",
                                                                   "parcel_dest_lon","parcel_dest_lat")],
                                                  1,vector_length)*distance_inflator
          #loop over carriers
          for(n in 1:N) {
            #when current position equals package destination, dropoff the parcel
            delivers<-filter(parcels_tbl_all,
                             (parcel_deliver_status==1)&(parcel_time_id_initial<=t)&
                               (abs(parcel_dest_lon-carriers[[n]]@position_current[1])==0)&
                               (abs(parcel_dest_lat-carriers[[n]]@position_current[2])==0)&
                               (parcel_carrier_id==carriers[[n]]@ID))
            if(nrow(delivers)>=1) {
              for(delivers_i in 1:nrow(delivers)) {
                carriers[[n]]<-dropoff(carriers[[n]],delivers$parcel_id[delivers_i])
                
              }}
           
            ###calculate carriers' theta
            if(nrow(carriers[[n]]@parcel_list)==0)
            {
              carriers_n_theta<-361
            }
            else{
              carriers_n_parcel_mean_lat<-mean(carriers[[n]]@parcel_list$parcel_dest_lat)
              carriers_n_parcel_mean_lon<-mean(carriers[[n]]@parcel_list$parcel_dest_lon)
              carriers_n_theta<-180/pi*acos((carriers_n_parcel_mean_lon-carriers[[n]]@position_current[1])/sqrt((carriers_n_parcel_mean_lon-carriers[[n]]@position_current[1])^2+
                                                                                                                  (carriers_n_parcel_mean_lat-carriers[[n]]@position_current[2])^2))
              carriers_n_theta<-((carriers_n_parcel_mean_lat-carriers[[n]]@position_current[2])>=0)*carriers_n_theta+((carriers_n_parcel_mean_lat-carriers[[n]]@position_current[2])<0)*
                carriers_n_theta
            }
            
            
            #opportunity of this carrier
            opportunities_n<-filter(opportunities,((carrier_id==carriers[[n]]@ID)&(parcel_deliver_status==0))|
                                      ((carrier_id==carriers[[n]]@ID)&(parcel_deliver_status==-1)&(parcel_carrier_id==carriers[[n]]@ID)))
            
            opportunities_n$theta_between<-(carriers_n_theta<=360)*abs(opportunities_n$parcel_theta-carriers_n_theta)
            opportunities_n$theta_between<-pmin(360-opportunities_n$theta_between,opportunities_n$theta_between)
            opportunities_n<-filter(opportunities_n,theta_between<=opt_max_angle)
            
            if (t == (picking_start_time*60/time_unit)){
              pickups1<-filter(opportunities_n[order(opportunities_n$mintime_to_dest, decreasing=TRUE),][1,],(current_distance==0))
            }else if((nrow(carriers[[n]]@parcel_list)==0)||(nrow(opportunities_n)==0)){
              pickups1<-filter(opportunities_n,(current_distance==0)) } else {
                #calculate angle between carrier's direction and parcel's direction
                carrier_average_direction<-data.frame(carrier_current_lon=carriers[[n]]@position_current[1],
                                                      carrier_current_lat=carriers[[n]]@position_current[2],
                                                      carrier_dest_lon=mean(carriers[[n]]@parcel_list$parcel_dest_lon),
                                                      carrier_dest_lat=mean(carriers[[n]]@parcel_list$parcel_dest_lat))
                opportunities_n$angle_between<-apply(cbind(carrier_average_direction,
                                                           opportunities_n[,c("parcel_current_lon","parcel_current_lat",
                                                                              "parcel_dest_lon","parcel_dest_lat")]),1,"get_angle")
                meantime_to_dest <- mean(carriers[[n]]@parcel_list$mintime_to_dest)
                #calculate incremental time if picking up the potential parcel
                opportunities_n$increment_time <- third_side(opportunities_n$mintime_to_dest,
                                                             meantime_to_dest,opportunities_n$angle_between)+
                  opportunities_n$mintime_to_dest-meantime_to_dest
                
                # num_parcel <- nrow(carrier@parcel_list)
                # coords.mx <- as.matrix(carrier@parcel_list)
                # dist.mx <- dist(coords.mx)
                # # dist.mx <- dist(coords.mx)
                # # tsp.ins <- tsp_instance(coords.mx, dist.mx )
                # # tour <- run_solver(tsp.ins, method="2-opt")
                # tour <- solve_TSP(TSP(dist.mx), method="nearest_insertion", start=1L)
                # end_p <- tour[[num_parcel]]
                # total_shipping_time <- tour_length(tour)-dist.mx[[((end_p-2)*(end_p-1)/2+1)]]
                
                # #when distance=0, pick the parcel up
                # pickups1<-filter(opportunities_n,((current_distance==0)&
                #                                     (angle_between<=max_angle))||(parcel_OD_distance<=500))}
                # pickups1<-filter(opportunities_n,((current_distance==0)&
                #                                     (angle_between<=max_angle))||(mintime_to_dest<=meantime_to_dest*1.1))}
                pickups1<-filter(opportunities_n,((current_distance==0)&(angle_between<=max_angle0||
                                                                           (increment_time<=(10^((t - picking_start_time*60/time_unit)/((picking_end_time*60/time_unit)-(picking_start_time*60/time_unit))-1))*meantime_to_dest))))
                
                # pickups1<-filter(opportunities_n,((current_distance==0)&
                #                                     (increment_time<=3)))
                
                
                
              }
            
            pickups2<-filter(parcels_tbl_all,(parcel_deliver_status==-1)&
                               (parcel_origin_lon==carriers[[n]]@position_current[1])&
                               (parcel_origin_lat==carriers[[n]]@position_current[2])&
                               (parcel_carrier_id==carriers[[n]]@ID))
            # if(length(which(pickups1$parcel_id%in%pickups2$parcel_id))>0)
            # {
            #   pickup_id<-c(pickups2$parcel_id,pickups1$parcel_id[-which(pickups1$parcel_id%in%pickups2$parcel_id)])
            # }
            # else{
            #   pickup_id<-c(pickups2$parcel_id,pickups1$parcel_id)
            # }
            pickup_id<-unique(c(pickups2$parcel_id,pickups1$parcel_id))
            current_item_cnt<-nrow(carriers[[n]]@parcel_list)
            if(length(pickup_id)>=1) {
              for(pickup_i in 1:length(pickup_id)) {
                if(current_item_cnt>carrier_capacity*(carriers[[n]]@group_id==0)+swot_capacity*(carriers[[n]]@group_id==1)) break
                carriers[[n]]<-pickup(carriers[[n]],pickup_id[pickup_i])
                current_item_cnt<-current_item_cnt+1
              }}
            
            if(carriers[[n]]@sleep_turns==1) {
              carriers[[n]]@sleep_turns<-0
              next}
            if(length(pickup_id)+nrow(delivers)>=1) {
              carriers[[n]]@sleep_turns<-0}
            #carrier thinks and make choice
            #calculate current parcel's urgency
            if(nrow(carriers[[n]]@parcel_listst)==0) {
              carrier_urgency<--999999
            } else if(nrow(carriers[[n]]@parcel_list)>=carrier_capacity*(carriers[[n]]@group_id==0)+swot_capacity*(carriers[[n]]@group_id==1)) {
              carrier_urgency<-Inf}
            else {
              #mark:full loaded scenario
              min_remaining_time<-min(parcels_tbl_all[(parcels_tbl_all$parcel_deliver_status==1)&
                                                        (parcels_tbl_all$parcel_carrier_id==carriers[[n]]@ID),"parcel_deadline"])-t
              
              carrier_urgency<-exp(required_shipping_time-2-min_remaining_time)}
         
            opportunities_n<-filter(opportunities_n,current_distance>10^(-5))
            carriers[[n]]<-think(carriers[[n]])
            carriers[[n]]@speed<-carrier_speed_mm
            #take action based on think result
            carriers[[n]]<-move(carriers[[n]],time_unit)
            carriers_tbl_all[carriers_tbl_all$carrier_id==n,
                             c("carrier_current_lon","carrier_current_lat","carrier_dest_lon","carrier_dest_lat")]<-
              c(carriers[[n]]@position_current,carriers[[n]]@position_dest)
            # record carrier track
            this_carrier_records <- data.frame(time_id=t,
                                               group_id=0,
                                               carrier_id=carriers[[n]]@ID,
                                               carrier_current_lon=carriers[[n]]@position_current[1],
                                               carrier_current_lat=carriers[[n]]@position_current[2],
                                               carrier_dest_lon=carriers[[n]]@position_dest[1],
                                               carrier_dest_lat=carriers[[n]]@position_dest[2],
                                               distance_traveled=carriers[[n]]@distance_traveled,
                                               parcel_type=nrow(carriers[[n]]@parcel_list),
                                               parcel_volume=sum(carriers[[n]]@parcel_list$parcel_volume),
                                               pickup_cnt=length(pickup_id),
                                               dropoff_cnt=nrow(delivers))
            
            #carrier_records<-rbind(carrier_records,carrier_records)
            carrier_records <- data.frame(rbindlist(list(carrier_records,this_carrier_records)))
            
  
            #  print(paste("time=",t,",carrier",carriers[[n]]@ID,
            #              ",picked up",sum((parcels_tbl_all$parcel_deliver_status==1)*(parcels_tbl_all$parcel_carrier_id==carriers[[n]]@ID)),
            #              "current_choice is",choice))
            
          }
          
          print(paste("carrier number=",N,", time=",t,", picked up ",
                      as.integer(sum(parcels_tbl_all$parcel_volume[parcels_tbl_all$parcel_deliver_status==1])),
                      ", delivered ",
                      as.integer(sum(parcels_tbl_all$parcel_volume[parcels_tbl_all$parcel_deliver_status==2])),
                      ", unattended ",
                      as.integer(sum(parcels_tbl_all$parcel_volume[parcels_tbl_all$parcel_deliver_status<=0&parcels_tbl_all$parcel_time_id_initial<=t])),
                      ", demand unshown ",
                      as.integer(sum(parcels_tbl_all$parcel_volume[parcels_tbl_all$parcel_deliver_status<=0&parcels_tbl_all$parcel_time_id_initial>t])),
                      sep=""))
          
        }
        
        N_carrier<-N #number of carriers
        N_parcel<-sum(OD_demand$daily_volume) #number of parcels
        N_delivered<-sum(parcels_tbl_all$parcel_volume[parcels_tbl_all$parcel_deliver_status==2])
        parcel_per_carrier<-N_delivered/N_carrier #parcel per carrier
        carrier_work_time<-sqldf("select carrier_id,max(time_id) as work_hour
                                 from carrier_records
                                 where parcel_type>0
                                 group by 1")
        carrier_actual_work_time<-sqldf("select carrier_id,count(time_id) as actual_work_hour
                                        from carrier_records
                                        where parcel_type>0
                                        group by 1")
        
        avg_working_hour<-mean(carrier_work_time$work_hour)*time_unit/60-10 # average working hour
        avg_actual_working_hour<-mean(carrier_actual_work_time$actual_work_hour)*time_unit/60 # average working hour
        
        
        avg_parcel_on_carrier<-sqldf("select avg(t1.parcel_volume) as avg_parcel_volume from carrier_records t1
                                     left join carrier_work_time t2
                                     on t1.carrier_id=t2.carrier_id
                                     where t1.time_id<=t2.work_hour") # average parcel volume
        
        avg_shipping_time<-sqldf("select sum((parcel_deliver_time-parcel_time_id_initial)*parcel_volume)*5/60/sum(parcel_volume) as avg_shipping_time
                                 from parcels_tbl_all 
                                 where parcel_deliver_status==2")
        on_time_volume<-sum(parcels_tbl_all$parcel_volume[(parcels_tbl_all$parcel_deliver_status==2)&
                                                            (parcels_tbl_all$parcel_deliver_time<=parcels_tbl_all$parcel_deadline)])
        on_time_rate<-on_time_volume/sum(parcels_tbl_all$parcel_volume)
        total_distance<-sum(carrier_records$distance_traveled[carrier_records$time_id==t])
        distance_per_carrier <- total_distance/N_carrier
         
        this_result<-data.frame(Speed=speed_i,
                                opt_importance=opt_importance,
                                N_carrier=N_carrier,
                                N_parcel=N_parcel,
                                N_parcel_delivered=N_delivered,
                                parcel_per_carrier=parcel_per_carrier,
                                avg_working_hour=avg_working_hour,
                                avg_actual_working_hour=avg_actual_working_hour,
                                avg_parcel_on_carrier=avg_parcel_on_carrier,
                                avg_shipping_time=avg_shipping_time,
                                on_time_rate=on_time_rate,
                                distance_per_carrier=distance_per_carrier,
                                total_distance=total_distance)
        
        #result<-rbind(result,this_result)
       
        result <- data.frame(rbindlist(list(result,this_result)))
        
        plot(sqldf("select (parcel_deliver_time-parcel_time_id_initial)*5/60 as time, count(*) as delivers 
                   from parcels_tbl_all 
                   where parcel_deliver_status==2
                   group by 1"),type="h",main=paste("Speed: ",speed_i,"Km/h   ||","Number of carriers: ",N_carrier))
      }}
  }
  
  on_time_volume1<-sum(parcels_tbl_all$parcel_volume[(parcels_tbl_all$parcel_deliver_status==2)&
                                                      (parcels_tbl_all$parcel_deliver_time<=parcels_tbl_all$parcel_deadline-18)])
  on_time_rate1<-on_time_volume1/sum(parcels_tbl_all$parcel_volume)
  on_time_volume2<-sum(parcels_tbl_all$parcel_volume[(parcels_tbl_all$parcel_deliver_status==2)&
                                                       (parcels_tbl_all$parcel_deliver_time<=parcels_tbl_all$parcel_deadline-30)])
  on_time_rate2<-on_time_volume2/sum(parcels_tbl_all$parcel_volume)
  end_time_total <- proc.time()
  end_time_total-begin_time_total
  #print(paste("total_time=",end_time_total-begin_time_total)) 
  
  write.csv(carrier_records,paste("~/Shunfeng/",model_version,"/carrier_capacity_",carrier_capacity,
                                  "_N_carrier_",N_carrier,"_",N_swot_carrier,"_carrier_records_rbindlist2.csv",sep=""))
  write.csv(parcels_tbl_all,paste("~/Shunfeng/",model_version,"/carrier_capacity_",carrier_capacity,
                                  "_N_carrier_",N_carrier,"_",N_swot_carrier,"_parcel_records_rbindlist2.csv",sep=""))
