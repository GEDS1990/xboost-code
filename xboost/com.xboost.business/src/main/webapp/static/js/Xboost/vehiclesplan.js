$(document).ready(function(){
	var map = new BMap.Map("depots-map");
	var top_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});// 左上角，添加比例尺
	var top_left_navigation = new BMap.NavigationControl();  //左上角，添加默认缩放平移控件
	var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}); //右上角，仅包含平移和缩放按钮
	map.addControl(top_left_control);        
	map.addControl(top_left_navigation);     
	map.addControl(top_right_navigation);
	map.centerAndZoom('北京');
	
	var doc = document;
	
	//depot search 
	function depotSearch (selement,val){
		var table = $(selement).DataTable();
		table.search(val).draw(false);
	}
	//去除重复数组元素
	function unique(arr) {
		var result = [], hash = {};
		for (var i = 0, elem; (elem = arr[i]) != null; i++) {
			if (arr[i] == "") {
				continue;
			}
			if (!hash[elem]) {
				result.push(elem);
				hash[elem] = true;
			}
		}
		return result;
	}
	//去除重复数据 deport
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
	};
	//排序 
	function Sort_sequence (arr) {
		arr.sort(function  (a,b) {
			return a.sequence - b.sequence;
		});
		return arr;
	};
	function RideId_List (result,rideidlist) {
		var len1 = result.length;
    	var len2 = rideidlist.length;
    	var list = [];
    	for (var i=0;i<len2;i++)
    	{
    		var item = {};
    		var l = [];
    		for (var j=0;j<len1;j++)
    		{
    			if (rideidlist[i].RideId == result[j].RideId) 
    			{
    				var items = {};
    				items.sequence = result[j].sequence;
    				items.curLoc = result[j].curLoc;
    				items.carType = result[j].carType;
    				l.push(items);
    			}
    		}
    		item.RideId = rideidlist[i].RideId;
    		item.carList = rideidlist[i].carList;
    		item.carType = rideidlist[i].carType;
    		item.val = Sort_sequence(l);
    		list.push(item);
    	}
    	return list;
	};
	function add00(m){
		var r;
		if (m<10) 
		{
			r = '00'+m;
		}
		else if (m>=10 && m<100)
		{
			r = '0'+m;
		}
		else
		{
			r=m;
		}
		return r;
	};
	function conjoin (arr) {
		var len = arr.length;
		var r = "";
		if (len !=0)
		{
			for (var i=0;i<len;i++)
			{
				var add = i==len-1?arr[i].curLoc:arr[i].curLoc + " >> ";
				r += add;
			}
		}
		return r;
	};
	function creatSelect (arr) {
		var len = arr.length;
		var add = '';
		add += '<option></option>'
		for (var i=0;i<len;i++)
		{
			add += '<option value='+arr[i]+'>'+arr[i]+'</option>';
		}
		return add;
	};
	function creatEle (id,data) {
		var p = doc.getElementById(id);
		var r_tbody = doc.createElement('tbody');
		var len = data.length;
		if (len !=0) 
		{
			for (var i=0;i<len;i++)
			{
				var add='';
				var r_tr = doc.createElement('tr');
				add += '<td>Ride '+add00(data[i].RideId)+'</td>';
				add += '<td><span class="plancar"><span>'+conjoin(data[i].val)+'</span><button data-rideid='+data[i].RideId+'>View on Map</button>'+'</span></td>';
				add += '<td>'+data[i].carType+'</td>';
				add += '<td>Chosen:<span>--</span> <select>'+creatSelect(data[i].carList)+'</select> <button id="j-save-car">Submit</button></td>';
				r_tr.innerHTML = add;
				r_tbody.appendChild(r_tr);
			}
			p.appendChild(r_tbody);
		}
	};
	
	
	
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
            {"data":"RideId","name":"RideId"},
            {"data":"carType","name":"carType"},
            {"data":"sequence","name":"sequence"},
            {"data":"curLoc","name":"cur_loc"}
            
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
        	//console.log(data);
        	var result = data.data;
        	var ridelist = uniqeByKeys(result,['RideId']);
        	var list = RideId_List(result,ridelist);
        	console.log(list);
        	creatEle('VehiclesPlan',list);
        },
        "drawCallback":function  (settings) {
        	var api = this.api();
	        // 输出当前页的数据到浏览器控制台
	        var datas = api.rows( {page:'current'} ).data();
	        //////console.log(datas);
	        
        }
    });
	
	
	
	
	
	
});
