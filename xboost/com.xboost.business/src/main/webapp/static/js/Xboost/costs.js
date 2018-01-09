$(function (){
	
	var doc = document;
	
	
	/*
	 * 
	 * costs.jsp == SolutionCostController
	 * 
	 */
	(function  () {
		var costs = doc.getElementById('costs');
		if (costs) {
			var vmA = new Vue({
				el:'#cost-form-a',
				data:{
					cseen:true,
					sitePeople:'',
					collectPeople:'',
					depotcount:'',
					depotPeoplecount:'',
					depotAllPeople:'',
					full_salaty:'',
					full_days:'',
					part_wage:'',
					part_work:'',
					day_p_cost:'',
					day_allp_cost:'',
					line_cost:'',
					allcost:'',
					full_staff:'',
					part_staff:'',
					piece:'',//总件数
					branch_cost:""
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
					}
					
				},
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
					}
				},
				computed:{
					a:function (){
						this.sum23();
						this.sumG();
						this.sumK();
					}
				}
				
			});
			
			
			
			
			var vmB = new Vue({
				el:'#cost-form-b',
				data:{
					cseen:true,
					sitePeople:'',
					collectPeople:'',
					depotcount:'',
					depotPeoplecount:'',
					depotAllPeople:'',
					full_salaty:'',
					full_days:'',
					part_wage:'',
					part_work:'',
					day_p_cost:'',
					day_allp_cost:'',
					line_cost:'',
					allcost:'',
					full_staff:'',
					part_staff:'',
					piece:'',//总件数
					branch_cost:""
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
					}
					
				},
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
					}
					
				},
				computed:{
					a:function (){
						this.sum23();
						this.sumG();
						this.sumK();
					}
				}
			
			});
			
			
			//请求数据
			$.get("/costs/costInitData.json").done(function (data){
				console.log(data)
				var $res=data;
				if (data.modelType == 1) {
					$('#model-type').text("Serial Model");
					if (data) {
						$.get("/costs/cost.json",{"plan":"A"}).done(function (res){
							console.log(res.data)
							if (res.data[0].distribPeopleWork == null) {
								console.log(1)
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
								vmA.branch_cost = result.branchTransportCost;
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
								vmB.branch_cost = result.branchTransportCost;
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
							
							
							
							//点击保存或者跟新数据  上
							$('.cost-btn').click(function (){
								var _val = $('#costs-choose').val();
								//console.log(_val)
								if (_val == "a") {
									var data = $("#cost-form-a").serialize();
								}else if(_val == "b"){
									var data = $("#cost-form-b").serialize();
								}
								$.post(urlcost,data).done(function (res){
									console.log(res);
									if (res == "success") {
										window.location.reload();
									}
								}).fail(function  () {
									console.log("fail")
								});
							});
							//点击保存或者跟新数据 下
							$('.costs-btn').click(function (){
								var _val = $('#costss-choose').val();
								//console.log(_val)
								if (_val == "a") {
									var data = $("#cost-form-a").serialize();
								}else if(_val == "b"){
									var data = $("#cost-form-b").serialize();
								}
								$.post(urlcost,data).done(function (res){
									console.log(res);
									if (res == "success") {
										window.location.reload();
									}
								}).fail(function  () {
									console.log("fail")
								});
							});
							
						}).fail(function (){
							console.log("fail");
						})
						
					}
				}

				
				
				
			}).fail(function  () {
				console.log("fail");
			});
			
			
			//
	
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
					addthead+='<th>总车次</th>';
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
					addthead+='<th>总车次</th>';
					addthead+='</tr>';
					addthead+='<tr>';
					addthead+='<th>卸车网点</th>';
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
					addthead+='<th>卸车网点</th>';
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
			
			
			
			
			
			
			
		}
	}());
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});