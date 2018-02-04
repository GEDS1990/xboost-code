$(function (){
	
	var doc = document;
	
	
	/*
	 * 
	 * costs.jsp == SolutionCostController
	 * 
	 */
	//接力函数封装
	function totalVol (totalVolList) {
		var list = [];
		for (var i in totalVolList) {
			var item = {};
			item.siteCode = i;
			item.totalVol = totalVolList[i];
			list.push(item);
		}
		return list;
	};
	function siteinfo (siteInfoList) {
		var list = [];
		for (var i in siteInfoList) {
			list.push(siteInfoList[i]);
		}
		return list;
	};
	function relaylistInit (siteInfoList,totalVolList) {
		var slist = siteinfo(siteInfoList);
		var tlist = totalVol(totalVolList);
		var len = slist.length;
		var len_t = tlist.length;
		var list = [];
		if (len !=0) {
			for (var i=0;i<len;i++) {
				for (var j=0;j<len_t;j++) {
					var code_i = slist[i].siteCode;
					var code_j = tlist[j].siteCode;
					if (code_i == code_j) {
						var item = {};
						item.siteCode = slist[i].siteCode;
						item.siteType = slist[i].siteType;
						item.full = slist[i].fullTimeStaff1;
						item.part = slist[i].partTimeStaff1;
						item.perMan = 0;
						item.totalVol = Math.ceil(tlist[j].totalVol);
						list.push(item);
					}
				}
				
			}
		}
		return list;
		
	};
	function relaylist (siteInfoList) {
		var slist = siteinfo(siteInfoList);
		var len = slist.length;
		var list = [];
		for (var i=0;i<len;i++) {
			var item = {};
			item.siteCode = slist[i].siteCode;
			item.siteType = slist[i].siteType;
			item.full = slist[i].fullTimeStaff1;
			item.part = slist[i].partTimeStaff1;
			item.perMan = slist[i].perMan;
			item.totalVol = slist[j].totalVol;
			list.push(item);
		}
		return list;
	};
	(function  () {
		var costs = doc.getElementById('costs');
		if (costs) {
			var vmA = new Vue({
				el:'#cost-form-a',
				data:{
					serialSeen:false,//显示串点
					relaySeen:false,//显示接力
					mixedSeen:false,//显示综合
					sitePeople:'',//网点 人效
					collectPeople:'',//集散点人效
					depotcount:'',//支线总数量
					depotPeoplecount:'',//每条支线人数
					depotAllPeople:'',//支线总人数
					full_salaty:'',//月工资
					full_days:'',//全职天数
					part_wage:'',//兼职小时费用
					part_work:'',//兼职时间
					day_p_cost:'',//支线depot单日人工成本
					day_p_dis_cost:'',//单日集散点人工成本
					day_allp_cost:'',//单日每件人工成本
					line_cost:'',//支线运输成本每件
					allcost:'',//总成本每件
					full_staff:'',//全职人数
					part_staff:'',//兼职人数
					piece:'',//总件数
					sum3:"",//支线distrib.center单日人工成本
					sitelist:[]
				},
				methods:{
					sum23:function (){ //单日人工总成本
						var sum = (this.full_staff * this.full_salaty)/this.full_days + (this.part_staff*this.part_wage*this.part_work);
						//console.log((this.full_staff))
						var r = Math.round(sum);
						//console.log(Boolean(r))
						if (!Boolean(r)) {
							r=0
						}
						this.day_p_cost = r;
					},
					sumG:function (){
						this.day_allp_cost = (this.day_p_cost/this.piece).toFixed(2);
					},
					sumK:function (){
						this.allcost = (Number(this.day_allp_cost) + Number(this.line_cost)).toFixed(2);
					},
					input_full:function  (e) {
						var sum = e.perMan - e.full;
						if (sum < 0) 
						{
							e.full = e.perMan;
							e.part = 0;
						}
						else
						{
							e.part = sum;
						}
					},
					input_part:function  (e) {
						var sum = e.perMan - e.part;
						if (sum < 0) 
						{
							e.part = e.perMan;
							e.full = 0;
						}
						else
						{
							e.full = sum;
						}
					},
					relaySum:function  () {
						var list = this.sitelist,
							len = list.length,
							sum22 = "",
							sum33 = "";
						if (len != 0) {
							for (var i=0;i<len;i++) {
								var type = list[i].siteType;
								if (type == "depot") 
								{
									var price_full = Math.round((list[i].full * this.full_salaty)/this.full_days);
									var price_part = Math.round((list[i].part*this.part_wage*this.part_work));
									sum22 =  Math.round(sum22)+Math.round(price_full + price_part) ;
								}

								else
								{
									var price_full = Math.round((list[i].full * this.full_salaty)/this.full_days);
									var price_part = Math.round((list[i].part*this.part_wage*this.part_work));
									sum33 =  Math.round(sum33)+Math.round(price_full + price_part) ;
								}
							}
							console.log(sum22)
							console.log(sum33)
							this.day_p_cost = sum22;
							sum33==""?this.sum3=0:this.sum3=sum33;
							this.day_allp_cost = ((Number(this.day_p_cost)+Number(this.sum3))/this.piece).toFixed(2);
							this.allcost = (Number(this.day_allp_cost) + Number(this.line_cost)).toFixed(2);
						}
						
					}
					
				},
//				updated:function (){
//					var self = this;
//					this.$nextTick(function  () {
//						self.relaySum();
//					});
//				},
				watch:{
					depotPeoplecount:function  (val) {
						if (val == 0) {
							this.depotPeoplecount = 0;
						}
						this.depotAllPeople	 = this.depotcount * val;
						this.full_staff = this.depotAllPeople;
						this.part_staff = 0;
					},
					full_staff:function  (val) {
						if (val>this.depotAllPeople) {
							this.full_staff = this.depotAllPeople;
							this.part_staff = 0;
						}else if(val<0){
							this.full_staff = 0;
							this.part_staff = this.depotAllPeople;
						}else{
							this.part_staff = this.depotAllPeople-val;
						}
						
						
					},
					part_staff:function (val){
						if (val>this.depotAllPeople) {
							this.part_staff = this.depotAllPeople;
							this.full_staff= 0;
						}else if(val<0){
							this.part_staff = 0;
							this.full_staff = this.depotAllPeople;
						}else{
							this.full_staff = this.depotAllPeople-val;
						}
					},
					sitePeople:function  (val) {
						var list = this.sitelist;
						var len = list.length;
						for (var i=0;i<len;i++) {
							if (list[i].siteType == "depot") {
								list[i].perMan =Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].full = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].part = 0;
							}
						}
						this.sitelist = list;
					},
					collectPeople:function  (val) {
						var list = this.sitelist;
						var len = list.length;
						for (var i=0;i<len;i++) {
							if (list[i].siteType != "depot") {
								list[i].perMan = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].full = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].part = 0;
							}
						}
						this.sitelist = list;
					}
				},
				computed:{
					a:function (){
						if (this.serialSeen == true) 
						{
							this.sum23();
							this.sumG();
							this.sumK();
						}
						if (this.relaySeen == true)
						{
							this.relaySum();
						}
						
					}
				}
				
			});
			
			
			
			
			var vmB = new Vue({
				el:'#cost-form-b',
				data:{
					serialSeen:false,//显示串点
					relaySeen:false,//显示接力
					mixedSeen:false,//显示综合
					sitePeople:'',//网点 人效
					collectPeople:'',//集散点人效
					depotcount:'',//支线总数量
					depotPeoplecount:'',//每条支线人数
					depotAllPeople:'',//支线总人数
					full_salaty:'',//月工资
					full_days:'',//全职天数
					part_wage:'',//兼职小时费用
					part_work:'',//兼职时间
					day_p_cost:'',//支线depot单日人工成本
					day_p_dis_cost:'',//单日集散点人工成本
					day_allp_cost:'',//单日每件人工成本
					line_cost:'',//支线运输成本每件
					allcost:'',//总成本每件
					full_staff:'',//全职人数
					part_staff:'',//兼职人数
					piece:'',//总件数
					sum3:"",//支线distrib.center单日人工成本
					sitelist:[]
				},
				methods:{
					sum23:function (){ //单日人工总成本
						var sum = (this.full_staff * this.full_salaty)/this.full_days + (this.part_staff*this.part_wage*this.part_work);
						//console.log((this.full_staff))
						var r = Math.round(sum);
						//console.log(Boolean(r))
						if (!Boolean(r)) {
							r=0
						}
						this.day_p_cost = r;
					},
					sumG:function (){
						this.day_allp_cost = (this.day_p_cost/this.piece).toFixed(2);
					},
					sumK:function (){
						this.allcost = (Number(this.day_allp_cost) + Number(this.line_cost)).toFixed(2);
					},
					input_full:function  (e) {
						var sum = e.perMan - e.full;
						if (sum < 0) 
						{
							e.full = e.perMan;
							e.part = 0;
						}
						else
						{
							e.part = sum;
						}
					},
					input_part:function  (e) {
						var sum = e.perMan - e.part;
						if (sum < 0) 
						{
							e.part = e.perMan;
							e.full = 0;
						}
						else
						{
							e.full = sum;
						}
					},
					relaySum:function  () {
						var list = this.sitelist,
							len = list.length,
							sum22 = "",
							sum33 = "";
						if (len != 0) {
							for (var i=0;i<len;i++) {
								var type = list[i].siteType;
								if (type == "depot") 
								{
									var price_full = Math.round((list[i].full * this.full_salaty)/this.full_days);
									var price_part = Math.round((list[i].part*this.part_wage*this.part_work));
									sum22 =  Math.round(sum22)+Math.round(price_full + price_part) ;
								}

								else
								{
									var price_full = Math.round((list[i].full * this.full_salaty)/this.full_days);
									var price_part = Math.round((list[i].part*this.part_wage*this.part_work));
									sum33 =  Math.round(sum33)+Math.round(price_full + price_part) ;
								}
							}
							console.log(sum22)
							console.log(sum33)
							this.day_p_cost = sum22;
							sum33==""?this.sum3=0:this.sum3=sum33;
							this.day_allp_cost = ((Number(this.day_p_cost)+Number(this.sum3))/this.piece).toFixed(2);
							this.allcost = (Number(this.day_allp_cost) + Number(this.line_cost)).toFixed(2);
						}
						
					}
					
				},
//				updated:function (){
//					var self = this;
//					this.$nextTick(function  () {
//						self.relaySum();
//					});
//				},
				watch:{
					depotPeoplecount:function  (val) {
						if (val == 0) {
							this.depotPeoplecount = 0;
						}
						this.depotAllPeople	 = this.depotcount * val;
						this.full_staff = this.depotAllPeople;
						this.part_staff = 0;
					},
					full_staff:function  (val) {
						if (val>this.depotAllPeople) {
							this.full_staff = this.depotAllPeople;
							this.part_staff = 0;
						}else if(val<0){
							this.full_staff = 0;
							this.part_staff = this.depotAllPeople;
						}else{
							this.part_staff = this.depotAllPeople-val;
						}
						
						
					},
					part_staff:function (val){
						if (val>this.depotAllPeople) {
							this.part_staff = this.depotAllPeople;
							this.full_staff= 0;
						}else if(val<0){
							this.part_staff = 0;
							this.full_staff = this.depotAllPeople;
						}else{
							this.full_staff = this.depotAllPeople-val;
						}
					},
					sitePeople:function  (val) {
						var list = this.sitelist;
						var len = list.length;
						for (var i=0;i<len;i++) {
							if (list[i].siteType == "depot") {
								list[i].perMan =Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].full = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].part = 0;
							}
						}
						this.sitelist = list;
					},
					collectPeople:function  (val) {
						var list = this.sitelist;
						var len = list.length;
						for (var i=0;i<len;i++) {
							if (list[i].siteType != "depot") {
								list[i].perMan = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].full = Math.ceil(Number(list[i].totalVol)/Number(val)) ;
								list[i].part = 0;
							}
						}
						this.sitelist = list;
					}
				},
				computed:{
					a:function (){
						if (this.serialSeen == true) 
						{
							this.sum23();
							this.sumG();
							this.sumK();
						}
						if (this.relaySeen == true)
						{
							this.relaySum();
						}
						
					}
				}
			
			});
			
			
			//请求数据
			$.get("/costs/costInitData.json").done(function (data){
				console.log(data)
				var $res=data;
				if (data.modelType == 1) {
					vmA.serialSeen = true;
					vmB.serialSeen = true;
					$('#model-type').text("Serial Model");
					if (data) {
						$.get("/costs/cost.json",{"plan":"A"}).done(function (res){
							console.log(res.data)
							if (res.data[0].distribPeopleWork == null) {
								var urlcost = "/costs/edit";
								vmA.sitePeople = $res.sitePeopleWork;
								vmA.collectPeople = $res.distribPeopleWork;
								vmA.depotcount = $res.siteCount;
								vmA.depotPeoplecount = 1;
								vmA.piece = $res.totalPiece;
								vmA.line_cost = Number($res.branchTransportCost).toFixed(2);
								vmA.full_salaty = 6000;
								vmA.full_days = 30;
								vmA.part_wage = 20;
								vmA.part_work = 2;
								vmB.sitePeople = $res.sitePeopleWork;
								vmB.collectPeople = $res.distribPeopleWork;
								vmB.depotcount = $res.siteCount;
								vmB.depotPeoplecount = 1;
								vmB.piece = $res.totalPiece;
								vmB.line_cost = Number($res.branchTransportCost).toFixed(2);
								vmB.full_salaty = 6000;
								vmB.full_days = 30;
								vmB.part_wage = 20;
								vmB.part_work = 2;
							}else{
								var result = res.data[0];
								console.log(result)
								var urlcost = "/costs/edit"; 
								vmA.sitePeople = result.sitePeopleWork;
								vmA.collectPeople = result.distribPeopleWork;
								vmA.depotcount = result.siteCount;
								vmA.piece = $res.totalPiece;
								vmA.depotPeoplecount = result.peopleNumPerSite;
								vmA.depotAllPeople = Number(result.peopleNumPerSite)*Number(result.siteCount);
								setTimeout(function (){
									vmA.full_staff = result.fullTimeStaff;
									vmA.part_staff = result.partTimeStaff;
								},100)
								vmA.full_salaty = result.fullTimeSalary;
								vmA.full_days = result.fullTimeWorkDay;
								vmA.part_wage = result.partTimeSalary;
								vmA.part_work = result.partTimeWorkDay;
								vmA.day_p_cost = result.sum2;
								vmA.day_allp_cost = result.totalDailyLaborCost;
								vmA.line_cost = result.branchTransportCost;
								vmA.allcost = result.totalCost;
								
								
								
								vmB.sitePeople = result.sitePeopleWork;
								vmB.collectPeople = result.distribPeopleWork;
								vmB.depotcount = result.siteCount;
								vmB.piece = $res.totalPiece;
								vmB.depotPeoplecount = result.peopleNumPerSite;
								vmB.depotAllPeople = Number(result.peopleNumPerSite)*Number(result.siteCount);
								setTimeout(function (){
									vmB.full_staff = result.fullTimeStaff;
									vmB.part_staff = result.partTimeStaff;
								},100)
								vmB.full_salaty = result.fullTimeSalary;
								vmB.full_days = result.fullTimeWorkDay;
								vmB.part_wage = result.partTimeSalary;
								vmB.part_work = result.partTimeWorkDay;
								vmB.day_p_cost = result.sum2;
								vmB.day_allp_cost = result.totalDailyLaborCost;
								vmB.line_cost = result.branchTransportCost;
								vmB.allcost = result.totalCost;
								
							}
						}).fail(function (){
							console.log("fail");
						})
						
					}
				}else if (data.modelType == 2) {
					vmA.relaySeen = true;
					vmB.relaySeen = true;
					$('#model-type').text("Relay Model");
					$.get("/costs/cost.json",{"plan":"A"}).done(function (res){
						console.log(res)
						if (res.data[0].distribPeopleWork == null) 
						{
							var _lista =  relaylistInit($res.siteInfoList,$res.totalVolList);
							var _listb =  relaylistInit($res.siteInfoList,$res.totalVolList);
							var len = _lista.length;
							var count_s = $res.sitePeopleWork;
							var count_d = $res.distribPeopleWork;
							vmA.sitePeople = $res.sitePeopleWork;
							vmA.collectPeople = $res.distribPeopleWork;
							for (var i=0;i<len;i++) {
								if (_lista[i].siteType == "depot") {
									var num = Number(_lista[i].totalVol)/Number(count_s);
									_lista[i].perMan = Math.ceil(num);
									_lista[i].full = Math.ceil(num);
									_lista[i].part = 0;
								}else{
									var num = Number(_lista[i].totalVol)/Number(count_d);
									_lista[i].perMan = Math.ceil(num);
									_lista[i].full = Math.ceil(num);
									_lista[i].part = 0;
								}
							}
							for (var i=0;i<len;i++) {
								if (_listb[i].siteType == "depot") {
									var num = Number(_listb[i].totalVol)/Number(count_s);
									_listb[i].perMan = Math.ceil(num);
									_listb[i].full = Math.ceil(num);
									_listb[i].part = 0;
								}else{
									var num = Number(_listb[i].totalVol)/Number(count_d);
									_listb[i].perMan = Math.ceil(num);
									_listb[i].full = Math.ceil(num);
									_listb[i].part = 0;
								}
							}
							vmA.piece = $res.totalPiece;
							vmA.line_cost = Number($res.branchTransportCost).toFixed(2);
							vmA.full_salaty = 6000;
							vmA.full_days = 30;
							vmA.part_wage = 20;
							vmA.part_work = 2;
							vmA.sitelist = _lista;
							
							vmB.sitePeople = $res.sitePeopleWork;
							vmB.collectPeople = $res.distribPeopleWork;
							vmB.piece = $res.totalPiece;
							vmB.line_cost = Number($res.branchTransportCost).toFixed(2);
							vmB.full_salaty = 6000;
							vmB.full_days = 30;
							vmB.part_wage = 20;
							vmB.part_work = 2;
							vmB.sitelist = _listb;
							
						}
						else
						{
							var result = res.data[0];
							var _lista = relaylist($res.siteInfoList);
							var _listb = relaylist($res.siteInfoList);
							vmA.sitelist = _lista;
							vmA.sitePeople = result.sitePeopleWork;
							vmA.collectPeople = result.distribPeopleWork;
							vmA.piece = $res.totalPiece;
							vmA.full_salaty = result.fullTimeSalary;
							vmA.full_days = result.fullTimeWorkDay;
							vmA.part_wage = result.partTimeSalary;
							vmA.part_work = result.partTimeWorkDay;
							vmA.day_p_cost = result.sum2;
							vmA.sum3 = result.sum3;
							vmA.day_allp_cost = result.totalDailyLaborCost;
							vmA.line_cost = result.branchTransportCost;
							vmA.allcost = result.totalCost;
							
							vmB.sitelist = _lista;
							vmB.sitePeople = result.sitePeopleWork;
							vmB.collectPeople = result.distribPeopleWork;
							vmB.piece = $res.totalPiece;
							vmB.full_salaty = result.fullTimeSalary;
							vmB.full_days = result.fullTimeWorkDay;
							vmB.part_wage = result.partTimeSalary;
							vmB.part_work = result.partTimeWorkDay;
							vmB.day_p_cost = result.sum2;
							vmB.sum3 = result.sum3;
							vmB.day_allp_cost = result.totalDailyLaborCost;
							vmB.line_cost = result.branchTransportCost;
							vmB.allcost = result.totalCost;
							
						}
								
							
					}).fail(function  () {
						console.log("fail");
					});
				}
				
				//点击保存或者跟新数据  上
				$('.cost-btn').click(function (){
					var _val = $('#costs-choose').val();
					if ($res.modelType == 1)
					{
						if (_val == "a") 
						{
							var data = $("#cost-form-a").serialize();
							//console.log(data)
						}
						else if(_val == "b")
						{
							var data = $("#cost-form-b").serialize();
						}
						$.post("/costs/edit",data).done(function (res){
							//console.log(res);
							if (res == "success") {
								window.location.reload();
							}
						}).fail(function  () {
							console.log("fail")
						});
					}
					else if ($res.modelType == 2)
					{
						if (_val == "a") {
							var _cost = {
								"sitePeopleWork":vmA.sitePeople,
								"distribPeopleWork":vmA.collectPeople,
								"fullTimeSalary":vmA.full_salaty,
								"fullTimeWorkDay":vmA.full_days,
								"partTimeSalary":vmA.part_wage,
								"partTimeWorkDay":vmA.part_work,
								"sum2":vmA.day_p_cost,
								"sum3":vmA.sum3,
								"totalDailyLaborCost":vmA.day_allp_cost,
								"branchTransportCost":vmA.line_cost,
								"totalCost":vmA.allcost
							}
							var SiteInfo = vmA.sitelist;
							var data = {
								"cost":_cost,
								"SiteInfo":SiteInfo
							}
							
						}else if(_val == "b"){
							var _cost = {
								"sitePeopleWork":vmB.sitePeople,
								"distribPeopleWork":vmB.collectPeople,
								"fullTimeSalary":vmB.full_salaty,
								"fullTimeWorkDay":vmB.full_days,
								"partTimeSalary":vmB.part_wage,
								"partTimeWorkDay":vmB.part_work,
								"sum2":vmB.day_p_cost,
								"sum3":vmB.sum3,
								"totalDailyLaborCost":vmB.day_allp_cost,
								"branchTransportCost":vmB.line_cost,
								"totalCost":vmB.allcost
							}
							var SiteInfo = vmB.sitelist;
							var data = {
								"cost":_cost,
								"SiteInfo":SiteInfo
							}
						}
						
						console.log(data);
						$.post("/costs/editRelay",data).done(function (res){
							console.log(res);
							if (res == "success") {
								window.location.reload();
							}
						}).fail(function  () {
							console.log("fail")
						});
					}
					
				});
				

				
				
				
			}).fail(function  () {
				console.log("fail");
			});
			

            //导出excel表格
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                var planA = $("#cost-form-a").serialize();
                var dataA = planA;

                var planB = $("#cost-form-b").serializeArray();
                var dataB = "";
                $.each(planB,function(idx,obj) {
                	console.log(obj.name);
                	console.log(obj.value);
                	dataB += obj.name + "B=" + obj.value +"&";
                });

                console.log(dataB+dataA);
                if (_xls) {
                    window.location.href="/costs/exportResult?"+dataB+dataA;
                }
                $(".modal-header span").trigger('click');
            });
		}
	}());
	
	
	/*
	 * efficiency.jsp == SolutionEfficiencyController
	 * 
	 */
	//将对象元素转换成字符串以作比较
	function obj2key(obj, keys){
	    var n = keys.length,
	        key = [];
	    while(n--){
	        key.push(obj[keys[n]]);
	    }
	    return key.join('|');
	}
	//去重操作
	function uniqeByKeys(array,keys){
	    var arr = [];
	    var hash = {};
	    for (var i = 0, j = array.length; i < j; i++) {
	        var k = obj2key(array[i], keys);
	        if (!(k in hash)) {
	            hash[k] = true;
	            arr .push(array[i]);
	        }
	    }
	    return arr ;
	}
	function startNum (a,b) {
		return a.start-b.start;
	}
	//处理返回数据
	function efficList (res){
		var arr = [];
		var allnewarr = []
		for (var i in res) {
			var obj = {}
			var s = i.split("-");
			s.push(res[i]);
			obj["site"] = s[0];
			obj["start"] = s[1];
			obj["end"] = s[2];
			obj["num"] = s[3];
			arr.push(obj);
		}
		//console.log(arr);
		var arrSite = uniqeByKeys(arr,["site"]);
		for (var j=0,alen = arrSite.length;j<alen;j++) {
			
			var newarr = {};
			var lista = [];
			for (var x=0,arrlen = arr.length;x<arrlen;x++) {
				var site1 = arrSite[j].site;
				var site2 = arr[x].site;
				if (site1 == site2) {
					var objj = {};
					objj["start"] = arr[x].start;
					objj["end"] = arr[x].end;
					objj["num"] = arr[x].num;
					lista.push(objj);
				}
			}
			lista.sort(startNum);
			var sum = "";
			var maxnum = lista[0].num;
			for (var y=0,listalen=lista.length;y<listalen;y++) {
				sum=Number(sum)+Number(lista[y].num);
				if (maxnum < lista[y].num) {
					maxnum = lista[y].num;
				}
			}
			newarr["site"]=arrSite[j].site;
			newarr["list"]=lista;
			newarr["sum"] = sum;
			newarr["max"] = maxnum;
			allnewarr.push(newarr);
		}
		return allnewarr;
	}
	
	//求时间
	function operationTime (data) {
		var result = parseInt(data),
	    	h = parseInt(result/60),
	    	m = result%60;
	    	return add0(h)+":"+add0(m);
	}
	function add0(m){return m<10?'0'+m:m };
	function addth (result){
		var addtime ='';
		for (var j=0,timelen=result.length;j<timelen;j++) {
			addtime+='<th>'+operationTime(result[j].start)+'-'+operationTime(result[j].end)+'</th>';
		}
		return addtime;
	}
	function addtd (result){
		var addtime ='';
		for (var j=0,timelen=result.length;j<timelen;j++) {
			addtime+='<td>'+result[j].num+'</td>';
		}
		return addtime;
	}
	(function  () {
		var efficiency = doc.getElementById('efficiency-car');
		if (efficiency) {
			//请求发出 车辆数
			$.get("/efficiency/leaveCarNum.json").done(function (res){
				//console.log(res)
				if (res) {
					var list = efficList(res);
					//console.log(list);
					//操作dom
					var result = list[0].list;
					//console.log(result)
					var allsum = "";
					var addthead="";
					
					for (var i=0,len = list.length;i<len;i++) {
						var addtbody ="";
						addtbody+='<tr>';
						addtbody+='<td>'+list[i].site+'</td>';
						addtbody+='<td>'+list[i].max+'</td>';addtd
						addtbody+=addtd(list[i].list);
						//console.log(list[i].list)
						addtbody+='<td>'+list[i].sum+'</td>';
						addtbody+='<td></td>';
						addtbody+='</tr>';
						allsum = Number(allsum)+Number(list[i].sum);
						$('#car-tbody').append(addtbody);
					}
					addthead+='<tr>';
					addthead+='<th colspan='+(result.length+3)+'>No. of Departing Vehicles</th>';
					addthead+='<th>Total Vehicles</th>';
					addthead+='</tr>';
					addthead+='<tr>';
					addthead+='<th>Depot ID</th>';
					addthead+='<th>Peak No. of Shipments</th>';
					addthead+=addth(result);
					addthead+='<th>Total Delivery Shipments</th>';
					addthead+='<th>'+allsum+'</th></tr>';
					$('#car-thead').append(addthead);	
					
				}
				
			}).fail(function (){
				console.log("fail")
			});
			
			//请求发出票数
			$.get("/efficiency/sbVol.json").done(function (res){
				//console.log(res);
				if (res) {
					var list = efficList(res);
					//console.log(list);
					//操作dom
					var result = list[0].list;
					//console.log(result)
					var allsum = "";
					var addthead="";
					
					for (var i=0,len = list.length;i<len;i++) {
						var addtbody ="";
						addtbody+='<tr>';
						addtbody+='<td>'+list[i].site+'</td>';
						addtbody+='<td>'+list[i].max+'</td>';addtd
						addtbody+=addtd(list[i].list);
						//console.log(list[i].list)
						addtbody+='<td>'+list[i].sum+'</td>';
						addtbody+='<td></td>';
						addtbody+='</tr>';
						allsum = Number(allsum)+Number(list[i].sum);
						$('#sbVol-tbody').append(addtbody);
					}
					addthead+='<tr>';
					addthead+='<th colspan='+(result.length+3)+'>No. of Delivery Shipments</th>';
					addthead+='<th>Total Shipments</th>';
					addthead+='</tr>';
					addthead+='<tr>';
					addthead+='<th>Depot ID</th>';
					addthead+='<th>Peak No. of Shipments</th>';
					addthead+=addth(result);
					addthead+='<th>Total Delivery Shipments</th>';
					addthead+='<th>'+allsum+'</th></tr>';
					$('#sbVol-thead').append(addthead);	
					
				}
			}).fail(function (){
				console.log("fail");
			});
			
			//请求到达车辆数
			$.get("/efficiency/arrCarNum.json").done(function (res){
				//console.log(res);
				if (res) {
					var list = efficList(res);
					//console.log(list);
					//操作dom
					var result = list[0].list;
					//console.log(result)
					var allsum = "";
					var addthead="";
					
					for (var i=0,len = list.length;i<len;i++) {
						var addtbody ="";
						addtbody+='<tr>';
						addtbody+='<td>'+list[i].site+'</td>';
						addtbody+='<td>'+list[i].max+'</td>';addtd
						addtbody+=addtd(list[i].list);
						//console.log(list[i].list)
						addtbody+='<td>'+list[i].sum+'</td>';
						addtbody+='<td></td>';
						addtbody+='</tr>';
						allsum = Number(allsum)+Number(list[i].sum);
						$('#arrcar-tbody').append(addtbody);
					}
					addthead+='<tr>';
					addthead+='<th colspan='+(result.length+3)+'>No. of Arraving vehicles</th>';
					addthead+='<th>Total Vehicles</th>';
					addthead+='</tr>';
					addthead+='<tr>';
					addthead+='<th>Depot ID</th>';
					addthead+='<th>Peak No. of Shipments</th>';
					addthead+=addth(result);
					addthead+='<th>Total Receiving Shipments</th>';
					addthead+='<th>'+allsum+'</th></tr>';
					$('#arrcar-thead').append(addthead);	
					
				}
			}).fail(function (){
				console.log("fail");
			});
			
			
			//请求到达票数
			$.get("/efficiency/unloadVol.json").done(function (res){
				//console.log(res);
				if (res) {
					var list = efficList(res);
					//console.log(list);
					//操作dom
					var result = list[0].list;
					//console.log(result)
					var allsum = "";
					var addthead="";
					
					for (var i=0,len = list.length;i<len;i++) {
						var addtbody ="";
						addtbody+='<tr>';
						addtbody+='<td>'+list[i].site+'</td>';
						addtbody+='<td>'+list[i].max+'</td>';addtd
						addtbody+=addtd(list[i].list);
						//console.log(list[i].list)
						addtbody+='<td>'+list[i].sum+'</td>';
						addtbody+='<td></td>';
						addtbody+='</tr>';
						allsum = Number(allsum)+Number(list[i].sum);
						$('#unloadVol-tbody').append(addtbody);
					}
					addthead+='<tr>';
					addthead+='<th colspan='+(result.length+3)+'>No. of Receiving Shipments</th>';
					addthead+='<th>Total Shipments</th>';
					addthead+='</tr>';
					addthead+='<tr>';
					addthead+='<th>Depot ID</th>';
					addthead+='<th>Peak No. of Shipments</th>';
					addthead+=addth(result);
					addthead+='<th>Total Receiving Shipments</th>';
					addthead+='<th>'+allsum+'</th></tr>';
					$('#unloadVol-thead').append(addthead);	
					
				}
			}).fail(function (){
				console.log("fail");
			});
			
			
			//网点信息
			$.get("/efficiency/siteInfo.json").done(function (res){
				//console.log(res);
				var _len = res.length;
				for (var i=0;i<_len;i++) {
					var add="";
					add += '<tr>';
					add += '<td>'+res[i].cur_loc+'</td>';
					add += '<td>'+Number(res[i].longitude).toFixed(6)+'</td>';
					add += '<td>'+Number(res[i].latitude).toFixed(6)+'</td>';
					add += '<td>'+(res[i].site_area==0?"-":Math.round(res[i].site_area) )+'</td>';
					add += '<td>'+res[i].site_type+'</td>';
					add += '<td>'+res[i].site_night_delivery+'</td>';
					add += '<td>'+(res[i].car_num >= 999?'∞':res[i].car_num)+'</td>';
					add += '<td>'+(res[i].large_carModle >= 999?'∞':res[i].large_carModle)+'</td>';
					add += '<td>'+res[i].max_operate_num+'</td>';
					add += '</tr>';
					$('#depotinfo-tbody').append(add);
				}
			}).fail(function  () {
				alert("fail");
			});

            //导出excel表格
            $('.export-btn').click(function  () {
                var _xls = $(this).attr('data-xls');
                if (_xls) {
                    window.location.href="/efficiency/exportResult";
                }
                $(".modal-header span").trigger('click');
            });
			
			
			
			
			
		}
	}());

	
	
	
	
	
	
	
	
	

	
	
});


//动态计算全职和兼职人数
//function inputChange  (e) {
//	var val = e.value;
//	var total = e.getAttribute("data-total");
//	var _input = e.parentNode.nextElementSibling.childNodes[1];
//	var sum = parseInt(total) - parseInt(val);
//	if (sum < 0) {
//		e.value = parseInt(total);
//		_input.value = 0
//	}else{
//		_input.value = sum
//	}
//	console.log(e.value);
//	console.log(_input.value);
//	
//};
//function inputChange1  (e) {
//	var val = e.value;
//	var total = e.getAttribute("data-total");
//	var _input = e.parentNode.previousElementSibling.childNodes[1];
//	var sum = parseInt(total) - parseInt(val);
//	if (sum < 0) {
//		e.value = parseInt(total);
//		_input.value = 0
//	}else{
//		_input.value = sum
//	}
//	console.log(e.value);
//	console.log(_input.value);
//};
