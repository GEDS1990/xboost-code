package com.mckinsey.ckc.sf.preparation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.mckinsey.ckc.sf.connection.JDBCConnection;
import com.mckinsey.ckc.sf.constants.IConstants;
import com.mckinsey.ckc.sf.data.CarrierRecords;
import com.mckinsey.ckc.sf.data.Coordinate;
import com.mckinsey.ckc.sf.data.GroupInfo;
import com.mckinsey.ckc.sf.data.OD_Demand;
import com.mckinsey.ckc.sf.data.Parcel;
import com.mckinsey.ckc.sf.data.Points;
import com.mckinsey.ckc.sf.main.Main;
import com.mckinsey.ckc.sf.utils.Grouping;

public class DataPreparation implements IConstants {

	public void loadParcelsFromDB(double carrierSpeed) {
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select demand_id," + "inbound_lon," + "inbound_lat," + "outbound_lon ," + "outbound_lat," + "daily_volume ," + "time_id " + "from od_demand";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int parcel_id = rs.getInt("demand_id");
				double inbound_long = rs.getDouble("inbound_lon");
				double inbound_lat = rs.getDouble("inbound_lat");
				double outbound_long = rs.getDouble("outbound_lon");
				double outbound_lat = rs.getDouble("outbound_lat");
				double daily_volume = rs.getDouble("daily_volume");
				int time_id = rs.getInt("time_id");
				// TODO timeId process
				Parcel parcel = new Parcel(parcel_id, 0, daily_volume, new Coordinate(inbound_long, inbound_lat), new Coordinate(outbound_long, outbound_lat),
				        new Coordinate(inbound_long, inbound_lat), 0, time_id, 0);
				parcel.setDeadline(time_id + 60 / TIME_UNIT * REQUIRED_SHIPPING_TIME);
				parcel.setDeliverTime(0);
				parcel.setPickupTime(0);
				parcel.setGroupID(0);
				double minToDest = Math.sqrt(Math.pow(inbound_long - outbound_long, 2) + Math.pow(inbound_lat - outbound_lat, 2)) * DISTANCE_INFLATOR / carrierSpeed;
				// calculate parcel theta
				double parcelTheta = 180 / Math.PI
				        * Math.asin((outbound_lat - inbound_lat) / Math.sqrt(Math.pow(outbound_long - inbound_long, 2) + Math.pow(outbound_lat - inbound_lat, 2)));
				parcel.setMinTimeToDest(minToDest);
				parcel.setParcelTheta(parcelTheta);
				Main.parcelMap.put(parcel_id, parcel);
				Main.totalVolume += daily_volume;
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Points> loadPoints() {
		List<Points> pointsList = new ArrayList<Points>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select point_id," + "point_lon," + "point_lat," + "initial_volume ," + "point_weight " + "from points";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int point_id = rs.getInt("point_id");
				double point_lon = rs.getDouble("point_lon");
				double point_lat = rs.getDouble("point_lat");
				double initial_volume = rs.getDouble("initial_volume");
				double point_weight = rs.getDouble("point_weight");
				Points p = new Points(point_id, point_lon, point_lat, initial_volume, point_weight);
				pointsList.add(p);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	// need pre-processing in R
	public List<GroupInfo> loadGroupFromDB() {
		List<GroupInfo> groupList = new ArrayList<GroupInfo>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select pre_group_id," + "origin_center_lat ," + "origin_center_lon," + "dest_center_lat," + "dest_center_lon ," + "origin_activate_time ,"
			        + "shipping_time ," + "volume ," + "group_n_carrier ," + "origin_deactivate_time," + "dest_activate_time," + "dest_deactivate_time " + "from group_info";

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int pre_group_id = rs.getInt("pre_group_id");
				double origin_center_lat = rs.getDouble("origin_center_lat");
				double origin_center_lon = rs.getDouble("origin_center_lon");
				double dest_center_lat = rs.getDouble("dest_center_lat");
				double dest_center_lon = rs.getDouble("dest_center_lon");
				int origin_deactivate_time = rs.getInt("origin_deactivate_time");
				int origin_activate_time = rs.getInt("origin_activate_time");
				int dest_activate_time = rs.getInt("dest_activate_time");
				int dest_deactivate_time = rs.getInt("dest_deactivate_time");
				int shipping_time = rs.getInt("shipping_time");
				int group_n_carrier = rs.getInt("group_n_carrier");
				double volume = rs.getDouble("volume");
				GroupInfo p = new GroupInfo(pre_group_id, new Coordinate(origin_center_lon, origin_center_lat), new Coordinate(dest_center_lon, dest_center_lat),
				        origin_activate_time, origin_deactivate_time, dest_activate_time, dest_deactivate_time, shipping_time, volume, group_n_carrier);
				groupList.add(p);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupList;
	}

	public List<Points> loadOriginalDemandFromDB(double carrierSpeed) {
		List<Points> pointsList = new ArrayList<Points>();
		List<OD_Demand> demandList = new ArrayList<OD_Demand>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select inbound_depot_id," + "outbound_depot_id," + "inbound_lon," + "inbound_lat," + "outbound_lon ," + "outbound_lat," + "date," + "hour," + "minute "
			        + "from demand3";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int inbound_depot_id = rs.getInt("inbound_depot_id");
				int outbound_depot_id = rs.getInt("outbound_depot_id");
				double inbound_long = rs.getDouble("inbound_lon");
				double inbound_lat = rs.getDouble("inbound_lat");
				double outbound_long = rs.getDouble("outbound_lon");
				double outbound_lat = rs.getDouble("outbound_lat");
				// String date = rs.getString("date");
				int hour = rs.getInt("hour");
				int minute = rs.getInt("minute");

				if (hour < DEMAND_CUT_TIME) {
					int minuteTime = minute / TIME_UNIT * TIME_UNIT;
					// double timeSimu = hour + minuteTime * 1.0 / 60;
					int timeID = hour * 60 / TIME_UNIT + minuteTime / TIME_UNIT;
					// TODO sequence
					if (timeID <= DEMAND_CUT_TIME * 60 / TIME_UNIT) {
						if (timeID <= PICKING_START_TIME * 60 / TIME_UNIT) {
							timeID = PICKING_START_TIME * 60 / TIME_UNIT;
						}
						OD_Demand demand = new OD_Demand(timeID, inbound_depot_id, inbound_long, inbound_lat, outbound_depot_id, outbound_long, outbound_lat);
						demandList.add(demand);
					}
				}
			}
			System.out.println("valid demand size is " + demandList.size());
			rs.close();
			stmt.close();
			// covert od_demand to group
			Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandsGrouping = Grouping.threeLevelGrouping(demandList);
			// TODO
			int demandId = 1;

			for (Entry<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandEntry : demandsGrouping.entrySet()) {
				for (Entry<Integer, Map<Integer, List<OD_Demand>>> secondDemand : demandEntry.getValue().entrySet()) {
					for (Entry<Integer, List<OD_Demand>> thirdDemand : secondDemand.getValue().entrySet()) {
						List<OD_Demand> demand = thirdDemand.getValue();
						OD_Demand sampleDemand = demand.get(0);
						double daily_volume = demand.size() * 1.0 / DATA_COMPLETE_RATE;
						int parcel_id = demandId++;
						double inbound_long = sampleDemand.getInboundLong();
						double inbound_lat = sampleDemand.getInboundLat();
						double outbound_long = sampleDemand.getOutboundLong();
						double outbound_lat = sampleDemand.getOutboundLat();
						int time_id = sampleDemand.getTimeId();
						// TODO timeId process
						Parcel parcel = new Parcel(parcel_id, 0, daily_volume, new Coordinate(inbound_long, inbound_lat), new Coordinate(outbound_long, outbound_lat),
						        new Coordinate(inbound_long, inbound_lat), 0, time_id, 0);
						parcel.setDeadline(time_id + 60 / TIME_UNIT * REQUIRED_SHIPPING_TIME);
						parcel.setDeliverTime(0);
						parcel.setPickupTime(0);
						parcel.setGroupID(0);
						double minToDest = Math.sqrt(Math.pow(inbound_long - outbound_long, 2) + Math.pow(inbound_lat - outbound_lat, 2)) * DISTANCE_INFLATOR / carrierSpeed;
						// calculate parcel theta
						double parcelTheta = 180 / Math.PI
						        * Math.asin((outbound_lat - inbound_lat) / Math.sqrt(Math.pow(outbound_long - inbound_long, 2) + Math.pow(outbound_lat - inbound_lat, 2)));
						parcel.setMinTimeToDest(minToDest);
						parcel.setParcelTheta(parcelTheta);
						Main.parcelMap.put(parcel_id, parcel);
						Main.totalVolume += daily_volume;
					}
				}
			}
			System.out.println("total volume is " + Main.totalVolume);
			System.out.println("total parcel count is " + Main.parcelMap.size());

			// initialize points
			int pointId = 1;
			Map<Integer, List<OD_Demand>> pointsGroup = demandList.stream().collect(Collectors.groupingBy(OD_Demand::getInboundDepotId));
			for (Entry<Integer, List<OD_Demand>> pointEntry : pointsGroup.entrySet()) {
				List<OD_Demand> demands = pointEntry.getValue();
				double sumVolume = demands.size() * 1.0 / DATA_COMPLETE_RATE;
				Points p = new Points();
				p.setPointID(pointId++);
				p.setPosition(new Coordinate(demands.get(0).getInboundLong(), demands.get(0).getInboundLat()));
				p.setInitialVolume(sumVolume);
				p.setPointWeight(sumVolume * 1.0 / Main.totalVolume);
				pointsList.add(p);
			}
			System.out.println("total points is " + pointsList.size());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	public List<Points> loadHubOriginalDemandFromDB() {
		List<Points> pointsList = new ArrayList<Points>();
		List<OD_Demand> demandList = new ArrayList<OD_Demand>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select inbound_depot_id," + "outbound_depot_id," + "inbound_lon," + "inbound_lat," + "outbound_lon ," + "outbound_lat," + "date," + "hour," + "minute "
			        + "from hub_demand2";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int inbound_depot_id = rs.getInt("inbound_depot_id");
				int outbound_depot_id = rs.getInt("outbound_depot_id");
				double inbound_long = rs.getDouble("inbound_lon");
				double inbound_lat = rs.getDouble("inbound_lat");
				double outbound_long = rs.getDouble("outbound_lon");
				double outbound_lat = rs.getDouble("outbound_lat");
				// String date = rs.getString("date");
				int hour = rs.getInt("hour");
				int minute = rs.getInt("minute");

				int minuteTime = minute / TIME_UNIT * TIME_UNIT;
				// double timeSimu = hour + minuteTime * 1.0 / 60;
				int timeID = hour * 60 / TIME_UNIT + minuteTime / TIME_UNIT;
				if (hour < DEMAND_CUT_TIME) {
					OD_Demand demand = new OD_Demand(timeID, inbound_depot_id, inbound_long, inbound_lat, outbound_depot_id, outbound_long, outbound_lat);
					System.err.println(demand.getTimeId() + "-" + demand.getInboundDepotId() + "-" + demand.getOutboundDepotId());
					demandList.add(demand);
				}
			}
			System.out.println("valid demand size is " + demandList.size());
			rs.close();
			stmt.close();

			// //covert od_demand to group
			Map<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandsGrouping = Grouping.threeLevelGrouping(demandList);
			// TODO
			int demandId = 0;
			for (Entry<Integer, Map<Integer, Map<Integer, List<OD_Demand>>>> demandEntry : demandsGrouping.entrySet()) {
				int timeID = demandEntry.getKey();
				for (Entry<Integer, Map<Integer, List<OD_Demand>>> secondDemand : demandEntry.getValue().entrySet()) {
					int inboundId = secondDemand.getKey();
					for (Entry<Integer, List<OD_Demand>> thirdDemand : secondDemand.getValue().entrySet()) {
						int outboundID = thirdDemand.getKey();
						if (demandsGrouping.get(timeID) == null || demandsGrouping.get(timeID).get(inboundId) == null
						        || demandsGrouping.get(timeID).get(inboundId).get(outboundID) == null) {
							List<OD_Demand> demand = thirdDemand.getValue();
							System.err.println(demand.size());
							System.err.println(timeID + "-" + inboundId + "-" + outboundID);
							System.err.println(demand.get(0).getTimeId() + "-" + demand.get(0).getInboundDepotId() + "-" + demand.get(0).getOutboundDepotId());

						} else {
							// int demand_count =
							// demandsGrouping.get(timeID).get(inboundId).get(outboundID).size();
							int parcel_id = demandId++;
							if (timeID <= DEMAND_CUT_TIME * 60 / TIME_UNIT) {
								if (timeID <= PICKING_START_TIME * 60 / TIME_UNIT) {
									timeID = PICKING_START_TIME * 60 / TIME_UNIT;
								}
								List<OD_Demand> demand = thirdDemand.getValue();
								OD_Demand sampleDemand = demand.get(0);
								double daily_volume = demand.size() * 1.0 / DATA_COMPLETE_RATE;
								double inbound_long = sampleDemand.getInboundLong();
								double inbound_lat = sampleDemand.getInboundLat();
								double outbound_long = sampleDemand.getOutboundLong();
								double outbound_lat = sampleDemand.getOutboundLat();
								// TODO timeId process
								Parcel parcel = new Parcel(parcel_id, 0, daily_volume, new Coordinate(inbound_long, inbound_lat), new Coordinate(outbound_long, outbound_lat),
								        new Coordinate(inbound_long, inbound_lat), 0, timeID, 0);
								parcel.setDeadline(timeID + 60 / TIME_UNIT * REQUIRED_SHIPPING_TIME);
								parcel.setDeliverTime(0);
								parcel.setPickupTime(0);
								parcel.setGroupID(0);
								Main.parcelMap.put(parcel_id, parcel);
								Main.totalVolume += daily_volume;
							}
						}
					}
				}
			}
			System.out.println("total volume is " + Main.totalVolume);
			System.out.println("total parcel count is " + Main.parcelMap.size());

			// initialize points
			int pointId = 1;
			Map<Integer, List<OD_Demand>> pointsGroup = demandList.stream().collect(Collectors.groupingBy(OD_Demand::getInboundDepotId));
			for (Entry<Integer, List<OD_Demand>> pointEntry : pointsGroup.entrySet()) {
				List<OD_Demand> demands = pointEntry.getValue();
				double sumVolume = demands.size() * 1.0 / DATA_COMPLETE_RATE;
				Points p = new Points();
				p.setPointID(pointId++);
				p.setPosition(new Coordinate(demands.get(0).getInboundLong(), demands.get(0).getInboundLat()));
				p.setInitialVolume(sumVolume);
				p.setPointWeight(sumVolume * 1.0 / Main.totalVolume);
				pointsList.add(p);
			}
			System.out.println("total points is " + pointsList.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	public List<GroupInfo> loadGroupOriginalDataFromDB(double carrierSpeedPerMm) {
		List<GroupInfo> groupList = new ArrayList<GroupInfo>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select parcel_group_id," + "parcel_id ," + "origin_center_id ," + "dest_center_id ," + "shipping_time," + "origin_center_lat ," + "origin_center_lon,"
			        + "dest_center_lat," + "dest_center_lon ," + "origin_activate_time ," + "origin_deactivate_time," + "dest_activate_time," + "dest_deactivate_time "
			        + "from group_data";

			ResultSet rs = stmt.executeQuery(sql);
			// merge information into parcel map
			while (rs.next()) {
				int parcel_id = rs.getInt("parcel_id");
				Parcel parcel = Main.parcelMap.get(parcel_id);
				if (parcel != null) {
					int parcel_group_id = rs.getInt("parcel_group_id");
					int origin_center_id = rs.getInt("origin_center_id");
					int dest_center_id = rs.getInt("dest_center_id");
					int shipping_time = rs.getInt("shipping_time");
					double origin_center_lat = rs.getDouble("origin_center_lat");
					double origin_center_lon = rs.getDouble("origin_center_lon");
					double dest_center_lat = rs.getDouble("dest_center_lat");
					double dest_center_lon = rs.getDouble("dest_center_lon");
					int origin_deactivate_time = rs.getInt("origin_deactivate_time");
					int origin_activate_time = rs.getInt("origin_activate_time");
					int dest_activate_time = rs.getInt("dest_activate_time");
					int dest_deactivate_time = rs.getInt("dest_deactivate_time");
					parcel.setParcel_group_id(parcel_group_id);
					parcel.setOrigin_center_id(origin_center_id);
					parcel.setDest_center_id(dest_center_id);
					parcel.setShipping_time(shipping_time);
					parcel.setOrigCenterPos(new Coordinate(origin_center_lon, origin_center_lat));
					parcel.setDestCenterPos(new Coordinate(dest_center_lon, dest_center_lat));
					parcel.setOrigin_activate_time(origin_activate_time);
					parcel.setOrigin_deactivate_time(origin_deactivate_time);
					parcel.setDest_activate_time(dest_activate_time);
					parcel.setDest_deactivate_time(dest_deactivate_time);
					// processing
					parcel.setPreParcelGroupId(Math.abs(parcel_group_id));
					parcel.setParcelRealDestPos(parcel.getDestPos());
					parcel.setParcelRealOriginPos(parcel.getOrigPos());
					parcel.setParcelRealTimeId(parcel.getTimeId());

					if (parcel.getGroupID() != 0) {
						parcel.setDestPos(parcel.getOrigCenterPos());
					}

					if (parcel.getOrigPos().equals(parcel.getDestPos())) {
						parcel.setParcelDeliverStatus(DELIVERED);
					}

					double minToDest = Math
					        .sqrt(Math.pow(parcel.getOrigPos().getX() - parcel.getDestPos().getX(), 2) + Math.pow(parcel.getOrigPos().getY() - parcel.getDestPos().getY(), 2))
					        * DISTANCE_INFLATOR / carrierSpeedPerMm;
					// calculate parcel theta
					double parcelTheta = 180 / Math.PI * Math.asin((-parcel.getDestPos().getY() - parcel.getOrigPos().getY()) / Math
					        .sqrt(Math.pow(parcel.getDestPos().getX() - parcel.getOrigPos().getX(), 2) + Math.pow(-parcel.getDestPos().getY() - parcel.getOrigPos().getY(), 2)));
					parcel.setMinTimeToDest(minToDest);
					parcel.setParcelTheta(parcelTheta);
					Main.parcelMap.put(parcel_id, parcel);
				} else {
					System.err.println("parcel id " + parcel_id + " in group could not find in demand");
				}
			}

			// System.out.println("group info size : "+groupCount);
			// group info
			// Map<Integer,GroupInfo> groupMap = new
			// HashMap<Integer,GroupInfo>();
			// for(Map.Entry<Integer, Parcel> parcelEntry :
			// Main.parcelMap.entrySet()){
			// Parcel p = parcelEntry.getValue();
			// int preGroupId = p.getPreParcelGroupId();
			//
			// if(groupMap.containsKey(preGroupId)){
			// GroupInfo g = groupMap.get(preGroupId);
			// double volume = g.getVolume()+p.getParcelVolumn();
			// g.setVolume(volume);
			// groupMap.put(preGroupId, g);
			// }else{
			// GroupInfo g = new GroupInfo(preGroupId,p.getOrigCenterPos(),
			// p.getDestCenterPos(),p.getOrigin_activate_time(),
			// p.getOrigin_deactivate_time(),p.getDest_activate_time(),
			// p.getDest_deactivate_time(), p.getShipping_time(),
			// p.getParcelVolumn(),2);
			// groupMap.put(preGroupId, g);
			// }
			// }

			// for(GroupInfo groupInfo : groupMap.values()){
			// double groupNCarrier =
			// groupInfo.getVolume()*1.0/CARRIER_MEANCAPACITY;
			// if(Math.ceil(groupNCarrier) > 2){
			// groupInfo.setGroupNCarrier((int)Math.ceil(groupNCarrier));
			// }
			// groupList.add(groupInfo);
			// }
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return groupList;
	}

	public List<Points> loadHubParcelAndGroupInfoFromDB(double carrierSpeedPerMm) {
		List<Points> pointsList = new ArrayList<Points>();
		List<OD_Demand> demandList = new ArrayList<OD_Demand>();
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();
			String sql = "select * from parcels_tbl_all";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				int parcel_id = rs.getInt("parcel_id");

				double parcel_origin_lon = rs.getDouble("parcel_origin_lon");
				double parcel_origin_lat = rs.getDouble("parcel_origin_lat");

				int parcel_inbound_id = rs.getInt("parcel_inbound_id");
				int parcel_carrier_id = rs.getInt("parcel_carrier_id");

				double parcel_current_lon = rs.getDouble("parcel_current_lon");
				double parcel_current_lat = rs.getDouble("parcel_current_lat");

				double parcel_dest_lon = rs.getDouble("parcel_dest_lon");
				double parcel_dest_lat = rs.getDouble("parcel_dest_lat");

				int parcel_outbound_id = rs.getInt("parcel_outbound_id");

				double parcel_volume = rs.getDouble("parcel_volume");
				double mintime_to_dest = rs.getDouble("mintime_to_dest");
				int parcel_time_id_initial = rs.getInt("parcel_time_id_initial");
				int parcel_deliver_status = rs.getInt("parcel_deliver_status");
				Parcel parcel = new Parcel(parcel_id, parcel_carrier_id, parcel_volume, new Coordinate(parcel_origin_lon, parcel_origin_lat),
				        new Coordinate(parcel_dest_lon, parcel_dest_lat), new Coordinate(parcel_current_lon, parcel_current_lat), mintime_to_dest, parcel_time_id_initial,
				        parcel_deliver_status);

				int parcel_deadline = rs.getInt("parcel_deadline");
				int parcel_deliver_time = rs.getInt("parcel_deliver_time");
				int parcel_pickup_time = rs.getInt("parcel_pickup_time");
				parcel.setDeadline(parcel_deadline);
				parcel.setDeliverTime(parcel_deliver_time);
				parcel.setPickupTime(parcel_pickup_time);

				int parcel_group_id = rs.getInt("parcel_group_id");
				int origin_center_id = rs.getInt("origin_center_id");
				int dest_center_id = rs.getInt("dest_center_id");
				int shipping_time = rs.getInt("shipping_time");
				double origin_center_lat = rs.getDouble("origin_center_lat");
				double origin_center_lon = rs.getDouble("origin_center_lon");
				double dest_center_lat = rs.getDouble("dest_center_lat");
				double dest_center_lon = rs.getDouble("dest_center_lon");
				int origin_deactivate_time = rs.getInt("origin_deactivate_time");
				int origin_activate_time = rs.getInt("origin_activate_time");
				int dest_activate_time = rs.getInt("dest_activate_time");
				int dest_deactivate_time = rs.getInt("dest_deactivate_time");

				int pre_parcel_group_id = rs.getInt("pre_parcel_group_id");

				parcel.setParcel_group_id(parcel_group_id);
				parcel.setOrigin_center_id(origin_center_id);
				parcel.setDest_center_id(dest_center_id);
				parcel.setShipping_time(shipping_time);
				parcel.setOrigCenterPos(new Coordinate(origin_center_lon, origin_center_lat));
				parcel.setDestCenterPos(new Coordinate(dest_center_lon, dest_center_lat));
				parcel.setOrigin_activate_time(origin_activate_time);
				parcel.setOrigin_deactivate_time(origin_deactivate_time);
				parcel.setDest_activate_time(dest_activate_time);
				parcel.setDest_deactivate_time(dest_deactivate_time);
				// processing
				parcel.setPreParcelGroupId(Math.abs(parcel_group_id));
				parcel.setParcelRealDestPos(parcel.getDestPos());
				parcel.setParcelRealOriginPos(parcel.getOrigPos());
				parcel.setParcelRealTimeId(parcel.getTimeId());
				parcel.setPreParcelGroupId(pre_parcel_group_id);

				// if(parcel.getGroupID() != 0){
				// parcel.setDestPos(parcel.getOrigCenterPos());
				// }
				//
				// if(parcel.getOrigPos().equals(parcel.getDestPos())){
				// parcel.setParcelDeliverStatus(DELIVERED);
				// }
				//
				// double minToDest =
				// Math.sqrt(Math.pow(parcel.getOrigPos().getX()-parcel.getDestPos().getX(),
				// 2)+Math.pow(parcel.getOrigPos().getY()-parcel.getDestPos().getY(),
				// 2))*DISTANCE_INFLATOR/carrierSpeedPerMm;
				// //calculate parcel theta
				double parcelTheta = 180 / Math.PI * Math.asin((-parcel.getDestPos().getY() - parcel.getOrigPos().getY())
				        / Math.sqrt(Math.pow(parcel.getDestPos().getX() - parcel.getOrigPos().getX(), 2) + Math.pow(-parcel.getDestPos().getY() - parcel.getOrigPos().getY(), 2)));
				// parcel.setMinTimeToDest(minToDest);
				parcel.setParcelTheta(parcelTheta);
				Main.parcelMap.put(parcel_id, parcel);
				Main.totalVolume += parcel.getParcelVolumn();

				OD_Demand demand = new OD_Demand(parcel_time_id_initial, parcel_inbound_id, parcel_current_lon, parcel_current_lat, parcel_outbound_id, parcel_dest_lon,
				        parcel_dest_lat);
				demandList.add(demand);
			}
			System.out.println("valid demand size is " + demandList.size());
			System.out.println("total Volume " + Main.totalVolume);
			rs.close();
			stmt.close();
			// initialize points
			int pointId = 1;
			Map<Integer, List<OD_Demand>> pointsGroup = demandList.stream().collect(Collectors.groupingBy(OD_Demand::getInboundDepotId));
			for (Entry<Integer, List<OD_Demand>> pointEntry : pointsGroup.entrySet()) {
				List<OD_Demand> demands = pointEntry.getValue();
				double sumVolume = demands.size() * 1.0 / DATA_COMPLETE_RATE;
				Points p = new Points();
				p.setPointID(pointId++);
				p.setPosition(new Coordinate(demands.get(0).getInboundLong(), demands.get(0).getInboundLat()));
				p.setInitialVolume(sumVolume);
				p.setPointWeight(sumVolume * 1.0 / Main.totalVolume);
				pointsList.add(p);
			}
			System.out.println("total points is " + pointsList.size());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return pointsList;
	}

	/**
	 * 
	 * @param carrierRecordLists
	 *            carrier record
	 */
	public void insertDynamicOutputCarrierRecordToDB(List<CarrierRecords> carrierRecordLists,String sdt) {
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();

			String tableName = "carrier_capacity_" + CARRIER_CAPACITY + "_N_carrier_" + N_CARRIER_RANGE + "_carrier_records_" + sdt;
			String sql = "CREATE TABLE " + tableName + " " + "(timeID INTEGER, " + " carrierID INTEGER, " + " groupID INTEGER, " + " distanceTraveled DOUBLE, "
			        + " parcelType INTEGER," + " parcelVolume DOUBLE, " + " pickupCount INTEGER," + " dropoffCount INTEGER," + " currentLong DOUBLE, " + " currentLat DOUBLE, "
			        + " destLong DOUBLE, " + " destLat DOUBLE " + ")";

			stmt.executeUpdate(sql);
			System.out.println("table created successfully:" + tableName);

			// insert carrier result into newly created table.
			for (CarrierRecords record : carrierRecordLists) {
				String query = "insert into "+tableName+"  (timeID,  carrierID,  groupID,  distanceTraveled,  parcelType, parcelVolume,  pickupCount, dropoffCount, currentLong,  currentLat,  destLong,  destLat ) values('" 
						+ record.getTimeID() + "','" 
						+ record.getCarrierID() + "','" 
						+ record.getGroupID() + "','" 
						+ record.getDistanceTraveled() + "','" 
						+ record.getParcelType() + "','" 
						+ record.getParcelVolume() + "','" 
						+ record.getPickupCount() + "','" 
						+ record.getDropoffCount() + "','" 
						+ record.getCurrentPos().getX() + "','" 
						+ record.getCurrentPos().getY()+ "','" 
						+ record.getDestPos().getX() + "','" 
						+ record.getDestPos().getY()
						+ "')";
				stmt.addBatch(query);
			}
			stmt.executeBatch();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void insertDynamicOutputParcelsToDB(String sdt) {
		try {
			Statement stmt = JDBCConnection.getConnection().createStatement();

			// create a new table with current date time
			String tableName = "carrier_capacity_" + CARRIER_CAPACITY + "_N_carrier_" + N_CARRIER_RANGE + "_parcel_records_" + sdt;
				
//			//for hub mode
//			private int parcel_group_id ;
//			private int origin_center_id ;
//			private int dest_center_id ;
//			private int shipping_time;
//			private Coordinate origCenterPos;
//			private Coordinate destCenterPos;
//			private int origin_deactivate_time ;
//			private int origin_activate_time ;
//			private int dest_activate_time;
//			private int dest_deactivate_time ;
//			
//			private int preParcelGroupId;
//			private Coordinate parcelRealDestPos;
//			private Coordinate parcelRealOriginPos;
//			private int parcelRealTimeId;
			String sql = "CREATE TABLE " + tableName + " " + "(parcelID INTEGER, " + " currentCarrierID INTEGER, " + " parcelVolumn DOUBLE, " + " minTimeToDest DOUBLE, "
			        + " timeId INTEGER," + " parcelDeliverStatus INTEGER, " + " deadline INTEGER," + " deliverTime INTEGER," + " pickupTime INTEGER, " + " groupID INTEGER, "
			        + " parcelTheta DOUBLE, " + " origLong DOUBLE, " + " origLat DOUBLE, " + " currentLong DOUBLE, " + " currentLat DOUBLE, " + " destLong DOUBLE, " + " destLat DOUBLE " + ")";

			stmt.executeUpdate(sql);
			System.out.println("table created successfully:" + tableName);

			// insert carrier result into newly created table.
			for (Map.Entry<Integer, Parcel> parcelEntry : Main.parcelMap.entrySet()) {
				Parcel parcel = parcelEntry.getValue();
				String query = "insert into "+tableName+"  (parcelID,  currentCarrierID,  parcelVolumn,  minTimeToDest,  timeId, parcelDeliverStatus,  deadline, deliverTime, pickupTime,  groupID,  parcelTheta,  origLong,  origLat,  currentLong,  currentLat,  destLong,  destLat )values('" 
						+ parcel.getParcelID() + "','" 
						+ parcel.getCurrentCarrierID() + "','" 
						+ parcel.getParcelVolumn()+ "','" 
						+ parcel.getMinTimeToDest() + "','" 
						+ parcel.getTimeId() + "','" 
						+ parcel.getParcelDeliverStatus() + "','" 
						+ parcel.getDeadline() + "','" 
						+ parcel.getDeliverTime() + "','" 
						+ parcel.getPickupTime() + "','" 
						+ parcel.getGroupID()+ "','" 
						+ parcel.getParcelTheta() + "','" 
						+ parcel.getOrigPos().getX() + "','" 
						+ parcel.getOrigPos().getY() + "','" 
					    + parcel.getCurrPos().getX() + "','" 
						+ parcel.getCurrPos().getY() + "','" 
						+ parcel.getDestPos().getX() + "','" 
						+ parcel.getDestPos().getY()
						+ "')";
				stmt.addBatch(query);
			}
			stmt.executeBatch();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public static void main(String args[]) {
//		DateFormat df = new SimpleDateFormat("yyyyMMdd_HH_mm");
//		String sdt = df.format(new Date(System.currentTimeMillis()));
//		String tableName = "carrier_capacity_" + CARRIER_CAPACITY + "_N_carrier_" + N_CARRIER_RANGE + "_carrier_records_" + sdt;
//		String sql = "CREATE TABLE " + tableName + " " + "(parcelID INTEGER, " + " currentCarrierID INTEGER, " + " parcelVolumn DOUBLE, " + " minTimeToDest DOUBLE, "
//		        + " timeId INTEGER," + " parcelDeliverStatus INTEGER, " + " deadline INTEGER," + " deliverTime INTEGER," + " pickupTime INTEGER, " + " groupID INTEGER, "
//		        + " parcelTheta DOUBLE, " + " origLong DOUBLE, " + " origLat DOUBLE, " + " currentLong DOUBLE, " + " currentLat DOUBLE, " + " destLong DOUBLE, " + " destLat DOUBLE " + ")";
//
//		System.out.println(sql);
//	}
}
