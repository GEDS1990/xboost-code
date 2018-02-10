#read in demand data
demand3<-read.csv("C:/Users/Xu Chen/Desktop/Related Documents/2017-03 顺丰/data/demand_data_12292016_v3.csv",stringsAsFactors = FALSE)
demand3 <- demand3[demand3$inbound_depot_id!=demand3$outbound_depot_id,]
#read in jiebo data
jiebo_selected<-read.csv("C:/Users/Xu Chen/Desktop/Related Documents/2017-03 顺丰/data/jieboselected_12292016_v1.csv",stringsAsFactors = FALSE)
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
demand3<-filter(demand3,date=="2016-10-25")


#############group data############################33
group_parcel_data<-read.csv("C:/Users/Xu Chen/Desktop/20170112group_parcel_data_1h.csv")
parcels_tbl_all<-merge(parcels_tbl_all,group_parcel_data,by.x="parcel_id",by.y="parcel_id",all.x=T,all.y=F)
parcels_tbl_all[is.na(parcels_tbl_all)]<-0
parcels_tbl_all$pre_parcel_group_id<-abs(parcels_tbl_all$parcel_group_id)
parcels_tbl_all$parcel_real_dest_lat<-parcels_tbl_all$parcel_dest_lat
parcels_tbl_all$parcel_real_dest_lon<-parcels_tbl_all$parcel_dest_lon
parcels_tbl_all$parcel_real_origin_lat<-parcels_tbl_all$parcel_origin_lat
parcels_tbl_all$parcel_real_origin_lon<-parcels_tbl_all$parcel_origin_lon
parcels_tbl_all$parcel_real_time_id_initial<-parcels_tbl_all$parcel_time_id_initial
parcels_tbl_all_backup<-parcels_tbl_all

parcels_tbl_all$parcel_dest_lat[which(parcels_tbl_all$parcel_group_id!=0)]=parcels_tbl_all$origin_center_lat[which(parcels_tbl_all$parcel_group_id!=0)]
parcels_tbl_all$parcel_dest_lon[which(parcels_tbl_all$parcel_group_id!=0)]=parcels_tbl_all$origin_center_lon[which(parcels_tbl_all$parcel_group_id!=0)]

parcels_tbl_all$parcel_deliver_status[
  which(parcels_tbl_all$parcel_origin_lon==parcels_tbl_all$parcel_dest_lon&
          parcels_tbl_all$parcel_origin_lat==parcels_tbl_all$parcel_dest_lat)]=2




parcels_tbl_all$mintime_to_dest <- sqrt((parcels_tbl_all$parcel_origin_lon-parcels_tbl_all$parcel_dest_lon)^2+
                                          (parcels_tbl_all$parcel_origin_lat-parcels_tbl_all$parcel_dest_lat)^2)*
  distance_inflator/carrier_speed_mm


# parcels_tbl_all<-parcels_tbl_all[which(parcels_tbl_all$pre_parcel_group_id%in%c(1,2,3)|(parcels_tbl_all$parcel_id<=3000&parcels_tbl_all$pre_parcel_group_id==0)),]
# parcels_tbl_all$parcel_id <- 1:nrow(parcels_tbl_all)
###Create Carriers
group_info <- sqldf("select pre_parcel_group_id as pre_group_id,
                    origin_center_lat as origin_center_lat,
                    origin_center_lon as origin_center_lon,
                    dest_center_lat as dest_center_lat,
                    dest_center_lon as dest_center_lon,
                    origin_activate_time as origin_activate_time,
                    origin_deactivate_time as origin_deactivate_time,
                    dest_activate_time as dest_activate_time,
                    dest_deactivate_time as dest_deactivate_time,
                    shipping_time as shipping_time,
                    sum(parcel_volume) as volume
                    from parcels_tbl_all
                    group by 1
                    ")
group_info$group_n_carrier<-pmax(2,ceiling(group_info$volume/carrier_meancapacity))