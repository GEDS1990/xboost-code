$(document).ready(function(){
	var dt = $("#SolutionVehiclesPlan").DataTable({
        "processing": true, //loding效果
        "serverSide":true, //服务端处理
        "searchDelay": 1000,//搜索延迟
        "destroy": true,
        "order":[[0,'desc']],//默认排序方式
        "lengthMenu":[100000],//每页显示数据条数菜单
        "ajax":{
            url:"/vehiclesPlan/vehiclesPlan.json", //获取数据的URL
            type:"get" //获取数据的方式
            
        },
        "columns":[  //返回的JSON中的对象和列的对应关系
            {"data":"id","name":"id"},
            {"data":"carName","name":"car_name"},
            {"data":"sequence","name":"sequence"},
            {"data":"curLoc","name":"cur_loc"},
            {"data":"siteName","name":"site_name"}
            
        ],
        "columnDefs":[ //具体列的定义
        	{
                "targets":[0],
                "visible":false
           }
        ],
        "language":{
            "lengthMenu":"Show _MENU_ Record",
            "search":"Search:",
            "info": "There are  _TOTAL_ records From _START_ To _END_",
            "processing":"Loading...",
            "zeroRecords":"No Data",
            "infoEmpty": "There are 0 records from 0 to 0",
            "infoFiltered":"(Read from _MAX_ record)",
            "paginate": {
                "first":      "First",
                "last":       "Last",
                "next":       "Next",
                "previous":   "Prev"
            }
        },
        "initComplete": function (settings, data) {
        	var $this = this;
        	//////console.log(data);
        	
        },
        "drawCallback":function  (settings) {
        	var api = this.api();
	        // 输出当前页的数据到浏览器控制台
	        var datas = api.rows( {page:'current'} ).data();
	        //////console.log(datas);
	        
        }
    });
	
	
	
	
	
	
});
