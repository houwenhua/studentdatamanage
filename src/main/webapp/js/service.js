$(function(){

	obj = {
		editRow:undefined,
		search:function(){
			$("#datagrid").datagrid('load',{
				servicename:$.trim($("input[name='servicename']").val()),
			});
		},
		//添加一行
		add:function(){
			var jq = top.jQuery;    
			var url = 'serviceadd.jsp';
            if (jq("#tabs").tabs('exists', '服务增加')){    
                jq("#tabs").tabs('select', '服务增加');    
            } else {  
            	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
                jq("#tabs").tabs('add',{    
                                    title:'服务增加',    
                                    content:content,    
                                    closable:true    
                                  });    
             }    
		},
		edit:function(){
			var rows = $("#datagrid").datagrid('getSelections');
			if(rows.length == 1){
				var jq = top.jQuery;    
				var url = 'serviceupdate.jsp?id='+rows[0].id;
	            if (jq("#tabs").tabs('exists', '服务更新')){    
	                jq("#tabs").tabs('select', '服务更新');    
	            } else {  
	            	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
	                jq("#tabs").tabs('add',{    
	                                    title:'服务更新',    
	                                    content:content,    
	                                    closable:true    
	                 });   
	             }   
			}else{
				$.messager.alert('警告','修改必须选中一行且只能选中一行','warning');
			}
			
		},
		remove:function(){
			var rows = $("#datagrid").datagrid('getSelections');
			if(rows.length>0){
				$.messager.confirm('提示','您确定删除这些记录吗？',function(flag){
					if(flag){
						var ids = [];
						for(var i = 0;i<rows.length;i++){
							ids.push(rows[i].id);
						}
						$.ajax({
							type:'post',
							url:'service_remove.action',
							data:{
								ids:ids.join(','),
							},
							beforeSend:function(){
								$("#datagrid").datagrid('loading');
							},
							success:function(data){
								if(data){
									$("#datagrid").datagrid('loaded');
									$("#datagrid").datagrid('load');
									$("#datagrid").datagrid('unselectAll');
									$.messager.show({
										title:'提示',
										msg:'删除成功了'+data,
									});
								}
							}
						});
					}
				});
			}else{
				$.messager.alert('提示','请选择你要删除的记录','info');
			}
		},
		image:function(){
			var rows = $("#datagrid").datagrid('getSelections');
			if(rows.length == 1){
				var jq = top.jQuery;    
				var url = 'serviceimageupload.jsp?id='+rows[0].id;
	            if (jq("#tabs").tabs('exists', '服务图片上传')){    
	                jq("#tabs").tabs('select', '服务图片上传');    
	            } else {  
	            	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
	                jq("#tabs").tabs('add',{    
	                                    title:'服务图片上传',    
	                                    content:content,    
	                                    closable:true    
	                 });   
	             }   
			}else{
				$.messager.alert('警告','修改必须选中一行且只能选中一行','warning');
			}
			
		}
		
	};
	
	
	$("#datagrid").datagrid({
		width:700,
		fit:true,
		url:'service_paginationQuery.action',
		title:'服务基本信息',
		striped:true,
		rownumbers:true,
		fitColumns:true,
		columns:[[
			{
				field:'id',
				title:'编号',
				width:100,
				checkbox:true,
			},
			{
				field:'servicename',
				title:'服务名',
				width:100,
				editor:{
					type:'validatebox',
					options:{
						required:true,
					},
				}
			},
			{
				field:'price',
				title:'全价',
				width:100,
				editor:{
					type:'validatebox',
					options:{
						required:true,
					},
				}
			},
			{
				field:'vipprice',
				title:'会员价',
				width:100,
				editor:{
					type:'validatebox',
					options:{
						required:false,
						validType:'eamil',
					},
				}
			},
			{
				field:'imagepath',
				title:'图片',
				width:100,
				editor:{
					type:'numberbox',
					options:{
						required:false,
					},
				}
			},
			{
				field:'remark',
				title:'备注',
				width:100,
				editor:{
					type:'textarea',
					options:{
						required:true,
					},
				}
			},
		]],
		toolbar:'#tb',
		pagination:true,
		pagePosition:'bottom',
		onDblClickRow:function(rowIndex,rowdata){
			var jq = top.jQuery;    
			var url = 'serviceupdate.jsp?id='+rowdata.id;
            if (jq("#tabs").tabs('exists', '服务更新')){    
                jq("#tabs").tabs('select', '服务更新');    
            } else {  
            	var content = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';     
                jq("#tabs").tabs('add',{    
                                    title:'服务更新',    
                                    content:content,    
                                    closable:true    
                 });   
             }   
		},
		onRowContextMenu:function(e,rowIndex,rowData){
			e.preventDefault();
			$("#menu").menu('show',{
				top:e.pageY,
				left:e.pageX,
			})
		}
	});
})
function reloaddatagrid(){
    $("#datagrid").datagrid('load');
}