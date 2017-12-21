$(function (){
	
	var doc = document;
	(function  () {
		var result_cost_item = doc.getElementsByClassName('result-cost-item');
		if (result_cost_item) {
			var vmA = new Vue({
				el:'#cost-form-a',
				data:{
					cseen:true,
					sitePeople:'300',
					collectPeople:'500',
					depotcount:'67',
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
					piece:'500'//网络
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
					},
					full_salaty:function  () {
						this.sum1();
						this.sumG();
					},
					full_days:function  () {
						this.sum1();
						this.sumG();
					},
					part_wage:function (){
						this.sum1();
						this.sumG();
					},
					part_work:function (){
						this.sum1();
						this.sumG();
					}
					
				}
				
			});
			
			
			
			
			var vmB = new Vue({
				el:'#cost-form-b',
				data:{
					cseen:true,
					sitePeople:'300',
					collectPeople:'500',
					depotcount:'67',
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
					piece:'500'//网络
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
						this.day_allp_cost = this.day_p_cost/this.piece;
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
					},
					full_salaty:function  () {
						this.sum1();
						this.sumG();
					},
					full_days:function  () {
						this.sum1();
						this.sumG();
					},
					part_wage:function (){
						this.sum1();
						this.sumG();
					},
					part_work:function (){
						this.sum1();
						this.sumG();
					}
					
				}
			
			});
	
		}
	}());
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
});