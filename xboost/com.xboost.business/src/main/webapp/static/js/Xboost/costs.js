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
					sum1:function (){ //单日人工总成本
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
					sumI:function (){
						this.line_cost = (this.branch_cost/this.piece).toFixed(2);
					},
					sumK:function (){
						this.allcost = Number(this.day_allp_cost) + Number(this.line_cost);
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
						
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					full_salaty:function  () {
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					full_days:function  () {
						this.sum1();
						this.sumG();
					},
					part_wage:function (){
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					part_work:function (){
						this.sum1();
						this.sumG();
						this.sumI();
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
					sum1:function (){ //单日人工总成本
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
					sumI:function (){
						this.line_cost = (this.branch_cost/this.piece).toFixed(2);
					},
					sumK:function (){
						this.allcost = Number(this.day_allp_cost) + Number(this.line_cost);
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
						
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
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					full_salaty:function  () {
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					full_days:function  () {
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					part_wage:function (){
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					},
					part_work:function (){
						this.sum1();
						this.sumG();
						this.sumI();
						this.sumK();
					}
					
				}
			
			});
			
			$('.cost-btn').click(function (){
				var _val = $('#cost-choose').val();
				if (_val == "a") {
					var data = $("#cost-form-a").serialize();
				}else{
					var data = $("#cost-form-b").serialize();
				}
				$.post("/costs/addCost",data).done(function (res){
					console.log(res)
				}).fail(function  () {
					console.log("fail")
				});
				
			})
			//请求数据
			$.get("/costs/costInitData.json").done(function (res){
				console.log(res)
				if (res) {
					vmA.sitePeople = res.sitePeopleWork;
					vmA.collectPeople = res.distribPeopleWork;
					vmA.depotcount = res.siteCount;
					vmA.piece = res.totalPiece;
					vmA.branch_cost = res.branchTransportCost;
					vmB.sitePeople = res.sitePeopleWork;
					vmB.collectPeople = res.distribPeopleWork;
					vmB.depotcount = res.siteCount;
					vmB.piece = res.totalPiece;
					vmB.branch_cost = res.branchTransportCost;
				}
			}).fail(function  () {
				console.log("fail");
			});
			
			$.get("/costs/cost.json",{"plan":"A"}).done(function (res){
				console.log(res)
				
			}).fail(function (){
				console.log("fail");
			})
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
			newarr["site"]=arrSite[j].site;
			newarr["list"]=lista;
			allnewarr.push(newarr);
		}
		return allnewarr;
	}
	(function  () {
		var efficiency = doc.getElementById('efficiency-car');
		if (efficiency) {
			$.get("/efficiency/leaveCarNum.json").done(function (res){
				console.log(res)
				var carlist = efficList(res);
				
				

				
				
				
			}).fail(function (){
				console.log("fail")
			});
		}
	}());
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});