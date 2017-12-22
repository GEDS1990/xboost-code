$(function (){
	
	var doc = document;
	(function  () {
		var result_cost_item = doc.getElementsByClassName('result-cost-item');
		if (result_cost_item) {
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
			
			//
	
		}
	}());
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});