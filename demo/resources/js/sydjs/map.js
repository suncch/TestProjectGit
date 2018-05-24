/**
 * Author: Allen
 * Date: 12/22/2011
 * Description: This is an interface for Baidu map, set some initial data, it can show them on the map.
 * how to use:
 *   showMap(parameter1, parameter2)  ---- parameter1 is a json object, parameter2 is the container div id
 */

/*
 my namespace allen
*/
var allen = {
    showMap: null,
    imageUrl: "./images/",
    chartUrl: "./js/chart/",
    mapType: '',
    dialogControl: null,
    controlTunnelId: null,
    mapData: null
};

/*
 here hide the internal functions and parameters
*/
(function() {
    var chartswf = {
        "column": allen.chartUrl+"Column3D.swf",
        "pie": allen.chartUrl+"Pie3D.swf"
    };
    
    // the map is shown circularly or not,1:circular(default) 0:no 2:no(hide the play control)
    // the current map will wait loadMapTimeout/1000 seconds or event "tilesloaded", then start timing.
    // this can avoid the page stopping because of net speed or other uncontroled situations.
    var  mapDivId = {};
    var descriptionDivWidth = "";
    var url = "";
    var clickTempParm = {};
    var selectMarker = null;

    var showBMap = allen.showMap = function(jsonObj, divId, baseUrl) {
    	$("#mapContainer").empty();
        if (this.initMapInfo(jsonObj, divId, baseUrl)) {
        	this.tempPoint = {};
        	this.allenLegendContent = {};
            this.startMap();
        }
    };

/*
     this is a function for setting the initial data, it will return a flag, true means initialization is ok
    */
    showBMap.prototype.initMapInfo = function(jsonObj, divId, baseUrl) {
        //var i, k, obj, objInner;
        var mypointGroup = jsonObj.mapArray;
        //var legendArray = jsonObj.legendContent;

        /*=== init global parameters ===*/
        mapDivId = divId;
        this.circleFlag = jsonObj.circleFlag;
        this.loadMapTimeout = jsonObj.loadMapTimeout;
        url = baseUrl || "";

        /*=== init map points info ===*/
        this.allenMapGroup = [];
        if (!mypointGroup || mypointGroup.length === 0) {
            return false;
        }
        this.curIndex = 0;
        for (var i = 0; i < mypointGroup.length; i++) {
        	this.allenMapGroup.push(mypointGroup[i]);
        }
        // if there is only one map, then it need not circle.
        if (this.allenMapGroup.length === 1 && this.circleFlag === 1) {
        	this.circleFlag = 0;
        }

        /*=== set legend content ===*/
        /*for (i = 1; i <= legendArray.length; i++) {
            obj = legendArray[i - 1];
            allenLegendContent["content_" + i] = "";

            for (k = 0; k < obj.length; k++) {
                objInner = obj[k];
                allenLegendContent["content_" + i] += '&nbsp;<span style="display:inline-block;background-color:' + objInner.legendColor + ';width:15px;height:15px"></span>&nbsp;' + objInner.legendText;
                if (k < obj.length - 1) {
                    allenLegendContent["content_" + i] += '&nbsp;<br>';
                }
            }
        }*/
        return true;
    };

/*
     start showing the map
    */
    var manager;
    var dataUpdateTimer;
    showBMap.prototype.startMap = function() {
    	try{
    	    this.map = {};
    		this.map = new BMap.Map(mapDivId);	
        // set some public events, they are used in all maps
    		this.setMapInitProperty();

    		this.loadMap();
    		var j = this;
        
        // creating or moving map to new center 
        if (this.zoomChange == '0') {
        	this.map.addEventListener("zoomend", function() {
        		j.changeZoomS();
	        },false);
        	
        } else if (this.zoomChange == '1') {
        	this.map.addEventListener("zoomend", function() {
        		j.changeZoomB();
	        },false);
        } else {
        	this.map.addEventListener("zoomend", function() {
        		j.changeZoom();
	        },false);
        }

        // delete baidu logo and copyright text
        
        //this.map.addEventListener("tilesloaded",
        //		this.deleteBaiduLogo);

        //showBMap.prototype.logoInterval = window.setInterval(function() {
        //	if(window.ActiveXObject) {
        //  		 CollectGarbage(); 
        //  	 }
        //}, 1000);
        
        showBMap.prototype.logoInterval = window.setInterval(function() {
            if ($("div[class=' anchorBL']").html() && $("#" + mapDivId).children().length === 13) {
                $("div[class=' anchorBL']").html("");
                $("#" + mapDivId + " div:last").html("");
                window.clearInterval(this.logoInterval);
            }
        }, 10);
    	}catch (error) {
    		dataUpdateTimer = window.setInterval (function (){ 
    			getSeconds();
			},1000 );
    		
    		setTimeout(function () { linkHomePage(); }, 6000);
    	};
    };
    var seconds = 5;
    var getSeconds = function() { 
    	manager = $.ligerDialog.waitting('网络连接出现问题，'+seconds+'秒后自动加载');
    	seconds--;
    };
    
    var linkHomePage = function() { 
    	manager.close();
    	window.clearInterval(dataUpdateTimer);
    	seconds = 5;
    	window.location.reload(true);
    };

/*
     delete baidu logo picture and copyright word, it is based on a div class name in baidu this.map api,
     so if it is changed, the function must be modified.
     copywrite div is the 11th child of the container div.
     U 'd better NOT using the setInterval, it made the performance worse!
     Set 2 future task (5ms and 100ms), they should be able to work.   
    */
    showBMap.prototype.deleteBaiduLogo = function() {
    	
    	//PlayControl= function() {};
        if(window.ActiveXObject) {
   		 CollectGarbage(); 
   	 }
        window.setTimeout(function() {
            $("div[class=' anchorBL']").html("");
            if ($("#" + mapDivId).children().length === 11) {
                $("#" + mapDivId + " div:last").html("");
            }
        }, 20);
        window.setTimeout(function() {
            $("div[class=' anchorBL']").html("");
            if ($("#" + mapDivId).children().length === 11) {
                $("#" + mapDivId + " div:last").html("");
            }
        }, 100);
    };

/*
     set some this.map's public events or controls, such as mouse dragging function 
    */
    showBMap.prototype.setMapInitProperty = function() {
        this.map.setMapType(BMAP_NORMAL_MAP); // this is this.map's default view

        this.map.enableScrollWheelZoom();
        if (this.circleFlag === 2) {
            this.map.enableDragging();
        }else{
            this.map.disableDragging();
        }
        this.map.disableDoubleClickZoom();

        this.map.setMinZoom(8);
        this.map.setMaxZoom(14);
        //this.map.addControl(new LegendControl());
        //this.map.addControl(new TitleControl());
        //this.map.addControl(new ContentControl());
        // if there are many this.map to showing, the control will be shown
        //if (allenMapGroup.length > 0) {
        //    this.map.addControl(new PlayControl());
        //}
        // add this.map view type control
        this.map.addControl(new BMap.MapTypeControl({
            mapTypes: [BMAP_NORMAL_MAP, BMAP_HYBRID_MAP, BMAP_SATELLITE_MAP]
        }));
        // add scale ruler control
        this.map.addControl(new BMap.ScaleControl({
            anchor: BMAP_ANCHOR_BOTTOM_LEFT
        }));
        // add zoom control
        this.map.addControl(new BMap.NavigationControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            type: BMAP_NAVIGATION_CONTROL_LARGE,
            showZoomInfo: true
        }));

        // when click the this.map, playing will be stop.ERROR:click event is not working rightly.
        //this.map.addEventListener("click", alert(2));
        var j = this;
        this.map.addEventListener("moveend", function() {
    		j.changePoint();
        },false);
        //this.map.addEventListener("dragend", deleteBaiduLogo);
    };
    
    showBMap.prototype.changePoint = function() { 
    	this.deleteBaiduLogo();
    	this.tempPoint.x=this.map.getCenter().lng;
    	this.tempPoint.y=this.map.getCenter().lat;
    };
    
    showBMap.prototype.changeZoom = function() { 
    	this.deleteBaiduLogo();
    	if (this.map.getZoom()>8 && allen.mapData.sub) {
    		this.zoomChange='1';
    		var jsonObj = jQuery.parseJSON(allen.mapData.sub.sub);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/map/";	    
		    delete this.map.__listeners;
		    $("#mapContainer").empty();
	        if (this.initMapInfo(globalJsonObj, "mapContainer",baseUrl)) {
	            this.startMap();
	        }
		    
		    
    	} else if (this.map.getZoom() < 9 && allen.mapData.sub) {
    		
    		 
        	//jQuery( "*", obj).add([obj]).each(function(){
        	//	jQuery.event.remove(this);
        	//	jQuery.removeData(this);
        	//	});
        	//	obj.innerHTML = "";
             this.zoomChange='0';
    		var jsonObj = jQuery.parseJSON(allen.mapData.main.main);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/this.map/";
		    delete this.map.__listeners;
		    $("#mapContainer").empty();
	        if (this.initMapInfo(globalJsonObj, "mapContainer",baseUrl)) {
	            this.startMap();
	        }
		   
    	}
    };
    
    showBMap.prototype.changeZoomB = function() {
    	this.deleteBaiduLogo();
    	
    	if (this.map.getZoom() < 9  && allen.mapData.sub) {
             this.zoomChange='0';
    		var jsonObj = jQuery.parseJSON(allen.mapData.main.main);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/map/";
		    delete this.map.__listeners;
		    $("#mapContainer").empty();
	        if (this.initMapInfo(globalJsonObj, "mapContainer",baseUrl)) {
	            this.startMap();
	        }
		    
    	}
    };
    
    showBMap.prototype.changeZoomS = function() {
    	this.deleteBaiduLogo();
    	if (this.map.getZoom()>8  && allen.mapData.sub) { 
             this.zoomChange='1';
    		var jsonObj = jQuery.parseJSON(allen.mapData.sub.sub);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/this.map/";
		    delete this.map.__listeners;
		    $("#mapContainer").empty();
	        if (this.initMapInfo(globalJsonObj, "mapContainer",baseUrl)) {
	            this.startMap();
	        }
		    
    	} 
    };



/*
     This is a core function for creating or moving this.map to new center
    */
    showBMap.prototype.loadMap = function() {
        // handle circle playing
        if (this.allenMapGroup.length === this.curIndex) {
            if (this.circleFlag === 1) {
            	this.curIndex = 0;
            } else {
                return;
            }
        }

        // means start loading
        this.mapLoaded = false;
        // clear all layers on the former this.map
        this.map.closeInfoWindow();
        this.map.clearOverlays();


        // get current this.map center, here is the ONLY ONE place to set the global value,
        // this is a basic global var used by all functions
        this.curPoint = this.allenMapGroup[this.curIndex];
        this.curIndex++;
        // if the this.map has some descriptions and the first is not blank, then add a blank description
        // it will look like better
        if (this.curPoint.description && this.curPoint.description.length > 0) {
            if (this.curPoint.description[0].type !== "blank") {
                this.curPoint.description.unshift({
                    type: "blank",
                    txt: "",
                    freq: 1
                });
            }
        }


        //============control start=======================
        // init or move this.map to the center point
        if (this.tempPoint.x && this.tempPoint.y) {
        	//this.map.panTo(new BMap.Point(this.tempPoint.x, this.tempPoint.y), {
            //    noAnimation: true
            //});
        	//this.map.setZoom(curPoint.zoom);
        	this.map.centerAndZoom(new BMap.Point(this.tempPoint.x, this.tempPoint.y), this.curPoint.zoom);
        } else {
        	if (this.map.getCenter().lng === 0 && this.map.getCenter().lat === 0) {
                this.map.centerAndZoom(new BMap.Point(this.curPoint.x, this.curPoint.y), this.curPoint.zoom);
            } else {
                this.map.panTo(new BMap.Point(this.curPoint.x, this.curPoint.y), {
                    noAnimation: true
                });
                this.map.setZoom(this.curPoint.zoom);
            }
        }
        

        // show or clear legend control
        if (this.curPoint.showLegend !== 0) {
            $('#map_legendDiv').html(this.allenLegendContent["content_" + this.curPoint.showLegend]);
        } else {
            $('#map_legendDiv').html("");
        }

        // show or clear title control
        if (this.curPoint.title && this.curPoint.title.length > 0) {
          //  $('#map_titleDiv').html(curPoint.title);
        	if (this.curPoint.titleColor && this.curPoint.titleColor.length > 0) {
        		$('#map_titleDiv').html(' <label style="color:'+this.curPoint.titleColor+';">'+this.curPoint.title+'</label>');
        	} else {
        		$('#map_titleDiv').html(this.curPoint.title);
        	}
        	
        } else {
            $('#map_titleDiv').html("");
        }

        // show or clear description control
        $('#map_contentTopDiv').html("");
        //============control end=======================


        //============OverLay start=======================
        // add the custom icon mark onto the this.map 
        this.addCustomMark();
        
        // show marker and polyline in the this.map
        this.addPolilineOnMap();
        //============OverLay end=======================

        // when the new this.map is loaded, the event will be touched
        //this.map.addEventListener("tilesloaded", this.currentMapPageInit);

        // if it can't load the this.map successful a long time(loadMapTimeout), it will do next work
        //window.setTimeout(function() {
        //    if (!this.mapLoaded) {
        //    	mapObj.currentMapPageInit();
        //    }
        //}, this.loadMapTimeout);
    };

/*
     After loading the this.map page, do some initial setiing for this
    */
    showBMap.prototype.currentMapPageInit = function() {
        // means this.map is loaded
    	this.mapLoaded = true;
        if (this.map==undefined) {
        	this.removeEventListener("tilesloaded", this.currentMapPageInit);
        } else {
        	this.map.removeEventListener("tilesloaded", this.currentMapPageInit);
        }
        $("#map_imgstop").attr("src", allen.imageUrl+"pause.png");


        // set current page showing time (milliseconds)
        var timeout = 0,
            sum = 0;
        this.curPoint.mapTimeout = this.curPoint.mapTimeout || 5000;
        if (this.curPoint.descriptionTimeout && this.curPoint.descriptionTimeout > 0 && this.curPoint.description && this.curPoint.description.length > 0) {
            for (var i = 0; i < this.curPoint.description.length; i++) {
                sum += this.curPoint.description[i].freq;
            }
            timeout = this.curPoint.descriptionTimeout * sum;
        }
        timeout = this.curPoint.mapTimeout > timeout ? this.curPoint.mapTimeout : timeout;
        //mapTimer = window.setTimeout(this.loadMap, timeout);


        fillCurrentMapPageDescription();

        this.deleteBaiduLogo();
    };

    var fillCurrentMapPageSummary = function(){
        var summaryContent = this.curPoint.summary;
        if (!summaryContent || summaryContent.length === 0) {
            return;
        }
        
        var opacityOld = $('#map_contentTopDiv').css("opacity");
        var width = summaryContent.width || 400;
        width = width + "px";
        
        $('#map_contentTopDiv').html("");
        
        $("#map_contentTopDiv").css({
            "width": width,
            opacity: 0
        }).animate({
            opacity: opacityOld
        }, 1000);
         
        $('#map_contentTopDiv').append("<div class='map_summaryDiv'><div>" + summaryContent.txt + "</div><div style='text-align:right'><img src='"+allen.imageUrl+"nav_up.png' class='map_gather_button'></div></div>");
        
        
        $(".map_gather_button").click(function() {
            stopShow('this.map');
            
            $('#map_contentTopDiv').html("");
            $('#map_contentTopDiv').append("<div class='map_summaryHideDiv'><div style='text-align:left'><img src='"+allen.imageUrl+"nav_down.png' class='map_spread_button'></div></div>");
            $("#map_contentTopDiv").css({
                "width": "30px"
            });
            
            $(".map_spread_button").click(function(){
                fillCurrentMapPageSummary();
            });
        });
    };

/*
     use current this.map's description fill the control div
    */
    var fillCurrentMapPageDescription = function() {
        var contentArray = this.curPoint.description;
        if (!contentArray || contentArray.length === 0) {
            fillCurrentMapPageSummary();
            return;
        }
        
        
        var index = 1;
        var count = 1;
        var divArray, divCurrent, sizeCurrent, chartIdNumber, isChartOk;
        var sizeArray = [];
        var timeout = this.curPoint.descriptionTimeout || 1000;


        $('#map_contentTopDiv').html("");
        for (var i = 0; i < contentArray.length; i++) {
            chartIdNumber = "map_chartId" + i;
            if (contentArray[i].type === "pic") {
                $('#map_contentTopDiv').append("<div class='map_contentDetailDiv'><div><img class='map_contentImageDiv'" + contentArray[i].txt + "></div><div style='text-align:right'><img src='"+allen.imageUrl+"nav_pre.png' class='map_prebutton'><img src='"+allen.imageUrl+"nav_next.png' class='map_nextbutton'><img class='map_closebutton' src='"+allen.imageUrl+"close.png'></div></div>");
            } else if (contentArray[i].type === "txt" && contentArray[i].txt.length > 0) {
                $('#map_contentTopDiv').append("<div class='map_contentDetailDiv'><div>" + contentArray[i].txt + "</div><div style='text-align:right'><img src='"+allen.imageUrl+"nav_pre.png' class='map_prebutton'><img  src='"+allen.imageUrl+"nav_next.png' class='map_nextbutton'><img class='map_closebutton' src='"+allen.imageUrl+"close.png'></div></div>");
            } else if (contentArray[i].type.slice(0, 4) === "json" && contentArray[i].chartData) {
                $('#map_contentTopDiv').append("<div class='map_contentDetailDiv'><div id='" + chartIdNumber + "'></div><div style='text-align:right'><img src='"+allen.imageUrl+"nav_pre.png' class='map_prebutton'><img  src='"+allen.imageUrl+"nav_next.png' class='map_nextbutton'><img class='map_closebutton' src='"+allen.imageUrl+"close.png'></div></div>");
                isChartOk = drawChart(chartIdNumber, contentArray[i].type.slice(5), contentArray[i].chartData);
                if (!isChartOk) {
                    $('#' + chartIdNumber).html("<b>The Chart is ERROR!</b>");
                }

            } else {
                $('#map_contentTopDiv').append("<div></div>");
            }
            if (contentArray[i].width && contentArray[i].width > 0) {
                sizeArray.push({
                    width: contentArray[i].width,
                    height: contentArray[i].height
                });
            } else {
                sizeArray.push({
                    width: 0,
                    height: 0
                });
            }
        }

        divArray = $("#map_contentTopDiv").children();
        divCurrent = divArray.eq(0);
        sizeCurrent = sizeArray[0];
        var slider = function() {
            if (index === divArray.size()) {
                clearInterval(this.descriptiontimer);
                return;
            }
            if (count < contentArray[index - 1].freq) {
                count++;
                return;
            }
            showcontrol();
        };
        this.descriptiontimer = setInterval(slider, timeout);


        $(".map_closebutton").click(function() {
            $('#map_contentTopDiv').html("");
            stopShow();
        });

        $(".map_prebutton").click(function() {
            for (var i = index - 1; i >= 1; i--) {
                if (contentArray[i - 1].chartData || (contentArray[i - 1].txt && contentArray[i - 1].txt.length > 0)) {
                    index = i - 1;
                    showcontrol();
                    break;
                }
            }
            stopShow();
        });

        $(".map_nextbutton").click(function() {
            for (var i = index + 1; i <= contentArray.length; i++) {
                if (contentArray[i - 1].chartData || (contentArray[i - 1].txt && contentArray[i - 1].txt.length > 0)) {
                    index = i - 1;
                    showcontrol();
                    break;
                }
            }
            stopShow();
        });

        var showcontrol = function() {
            sizeCurrent = sizeArray[index];
            if (sizeCurrent && sizeCurrent.width > 0) {
                var width = sizeCurrent.width + "px";
                var height = sizeCurrent.height + "px";
                if (descriptionDivWidth === "") {
                    descriptionDivWidth = $("#map_contentTopDiv").css("width");
                }
                $("#map_contentTopDiv").css({
                    "width": width
                });
                $("#map_chartId" + index).css({
                    "height": height
                });
            } else {
                if ($("#map_contentTopDiv").css("width") !== descriptionDivWidth) {
                    $("#map_contentTopDiv").css({
                        "width": descriptionDivWidth
                    });
                }
            }

            divCurrent.css({
                "display": "none",
                "z-index": 0
            });
            var divTmp = divArray.eq(index).css({
                "display": "block",
                "z-index": 0,
                opacity: 0
            }).animate({
                opacity: 1
            }, 1000);

            divCurrent.css({
                "opacity": "0"
            });
            divCurrent = divTmp.css({
                "z-index": 1
            });

            index++;
            count = 1;
        };
    };

/*
     stop the auto playing and current page description playing,
     type is u want to stop what, if it is null, wil stop all
    */
    var stopShow = function(type) {
        if (type === 'content') {
            clearInterval(this.descriptiontimer);
        } else if (type === 'this.map') {
            clearTimeout(this.mapTimer);
        } else {
            clearTimeout(this.mapTimer);
            clearInterval(this.descriptiontimer);
        }

        $("#map_imgstop").attr("src", allen.imageUrl+"stop.png");
    };

/*
     draw the chart, if an error happens whenloading the chart, it will return false
    */
    var drawChart = function(chartIdNumber, chartType, chartData) {
        try {
        	FusionCharts.items["myChart_" + chartIdNumber]=null;
            var myChart = new FusionCharts(chartswf[chartType], "myChart_" + chartIdNumber, "100%", "100%", "0", "1");
            myChart.setJSONData(chartData);
            myChart.render(chartIdNumber);
        } catch (exception) {
            return false;
        }
        return true;
    };

/*
     Add custom icon mark
    */
    var channelName = '';
    showBMap.prototype.addCustomMark = function() {
        if (!this.curPoint.iconMark || this.curPoint.iconMark.length === 0) {
            return;
        }

        var markObj, tipContent;
        var thisObj = this;
        for (var i = 0; i < this.curPoint.iconMark.length; i++) {
            markObj = this.curPoint.iconMark[i];
            var myicon = new BMap.Icon(markObj.iconName, new BMap.Size(markObj.imageWidth, markObj.imageHeight), {
                anchor: new BMap.Size(10, 25)
            });
            var mymarker = new BMap.Marker(new BMap.Point(markObj.x, markObj.y), {
                icon: myicon
            });
            mymarker.obj = markObj;
            // set marker tips
            if (markObj.iconTitle && markObj.iconTitle.length > 0) {
                mymarker.setTitle(markObj.iconTitle);
            }

            // show some detail info for the marked place, when click the marker and the same time pause the playing            
            if (markObj.events && markObj.events.length > 0) {
                for(var j=0; j<markObj.events.length; j++){
                    if(markObj.events[j]==="click"){
                        if(markObj.iconLink){
                            mymarker.addEventListener("click", function() {
                            	if (allen.mapType==='channelMain') {
                            		thisObj.addDialogDataOnMap(this.obj.iconTip,this.obj.iconLink.linkUrl);  
                                } else if (allen.mapType==='channelMonitor') {
                                	thisObj.addTabOnMap(this.obj.iconLink.linkUrl);
                                } else if (allen.mapType==='realTimeMonitor') {
                                	thisObj.addDialogControlOnMap(this.obj.iconTip,this.obj.iconLink.linkUrl);
                                }
                            	
                            });
                        }
                    }else if(markObj.events[j]==="mouseover"){
                        if (markObj.iconLink) {
                        	mymarker.addEventListener("mouseover", thisObj.addMarkerOnMap,false); 
                        	mymarker.addEventListener("mouseout", thisObj.reMarkerOnMap,false);
                        }                        
                    } 
                }
                
                
            }
            showBMap.prototype.reMarkerOnMap = function() {
            	if (allen.mapType) {
            		this.setTop(false);
            		if (this.obj.iconName.indexOf("alert")>-1) {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-alert.gif", new BMap.Size(23, 28), {
                            anchor: new BMap.Size(10, 25)
                        }));
            		} else {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-norm.gif", new BMap.Size(23, 28), {
                            anchor: new BMap.Size(10, 25)
                        }));
            		}
            		
            		
            		this.removeEventListener("click", thisObj.addClickOnMap,false);
                } 
            };
            
            showBMap.prototype.addMarkerOnMap = function() {
            	if (allen.mapType) {
            		clickTempParm.iconTip = this.obj.iconTip;
                    clickTempParm.linkUrl = this.obj.iconLink.linkUrl;
                    clickTempParm.highWayId = this.obj.highWayId;
            		this.setTop(true);
            		if (this.obj.iconName.indexOf("alert")>-1) {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-alert-over.gif", new BMap.Size(33, 38), {
                            anchor: new BMap.Size(20, 30)
                        }));
            		} else {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-norm-over.gif", new BMap.Size(33, 38), {
                            anchor: new BMap.Size(20, 30)
                        }));
            		}
            		this.addEventListener("click", thisObj.addClickOnMap,false);
            		
            		if (selectMarker) {
            			selectMarker.setTop(false);
            			if (selectMarker.obj.iconName.indexOf("alert")>-1) {
            				
            				selectMarker.setIcon(new BMap.Icon("../../images/map/tunnel-alert.gif", new BMap.Size(23, 28), {
                                anchor: new BMap.Size(10, 25)
                            }));
                		} else {
                			selectMarker.setIcon(new BMap.Icon("../../images/map/tunnel-norm.gif", new BMap.Size(23, 28), {
                                anchor: new BMap.Size(10, 25)
                            }));
                		}
            			
            			selectMarker = null;
            		}
                } else {

                    this.map.closeInfoWindow();
                    tipContent = this.obj.iconTip + "<br>";
                    this.openInfoWindow(new BMap.InfoWindow(tipContent, {
                        width: 160,
                        height:200,
                        enableAutoPan: false
                    })); 
                }
            };
            
            showBMap.prototype.addClickOnMap = function() {
            	if (allen.mapType==='channelMain') {
            		thisObj.addDialogDataOnMap(clickTempParm.iconTip,clickTempParm.linkUrl,clickTempParm.highWayId);    
                 } else if (allen.mapType==='channelMonitor') {
                    delete this.__listeners;	
                    thisObj.addTabOnMap(clickTempParm.linkUrl);
                 	this.addEventListener("mouseover", thisObj.addMarkerMonitorOnMap,false); 
                 	selectMarker = this;
                 } else if (allen.mapType==='realTimeMonitor') {
                	thisObj.addDialogControlOnMap(clickTempParm.iconTip,clickTempParm.linkUrl);
                 }
            };
            
            showBMap.prototype.addMarkerMonitorOnMap = function() {
            	if (allen.mapType) {
            		
            		clickTempParm.iconTip = this.obj.iconTip;
                    clickTempParm.linkUrl = this.obj.iconLink.linkUrl;
                    clickTempParm.highWayId = this.obj.highWayId;
            		
            		this.setTop(true);
            		
            		if (this.obj.iconName.indexOf("alert")>-1) {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-alert-over.gif", new BMap.Size(33, 38), {
                            anchor: new BMap.Size(20, 30)
                        }));
            		} else {
            			this.setIcon(new BMap.Icon("../../images/map/tunnel-norm-over.gif", new BMap.Size(33, 38), {
                            anchor: new BMap.Size(20, 30)
                        }));
            		}
            		this.addEventListener("click", thisObj.addClickOnMap,false);
            		this.addEventListener("mouseout", thisObj.reMarkerOnMap,false);
            		
            		if (selectMarker) {
            			selectMarker.setTop(false);
            			if (selectMarker.obj.iconName.indexOf("alert")>-1) {
            				
            				selectMarker.setIcon(new BMap.Icon("../../images/map/tunnel-alert.gif", new BMap.Size(23, 28), {
                                anchor: new BMap.Size(10, 25)
                            }));
                		} else {
                			selectMarker.setIcon(new BMap.Icon("../../images/map/tunnel-norm.gif", new BMap.Size(23, 28), {
                                anchor: new BMap.Size(10, 25)
                            }));
                		}
            			
            			selectMarker = null;
            		}
                } 
            };
            
            this.map.addOverlay(mymarker);
            
           /* if (i==0 && markObj.events[0]==="mouseover") {
            	for (var a = 0; a < this.map.getOverlays().length; a++) {
                    if (this.map.getOverlays()[a].toString().indexOf("Marker")>-1) {
                    	this.map.getOverlays()[a].addEventListener("mouseover", this.addMarkerOnMap,false); 
                    	this.map.getOverlays()[a].addEventListener("mouseout", this.reMarkerOnMap,false);
                      };	
                    }
            }*/
        }
        
    };

    var dialogObjForView = null;
    var dialogRealForView = null;
    var managerDialog = null;
    showBMap.prototype.addDialogDataOnMap = function(iconTip,linkUrl,highWayId) {
    	if (linkUrl.indexOf("main")>0) {
    		this.tempPoint.x=null;
        	this.tempPoint.y=null;
        	delete this.map.__listeners;
        	
    		var tunnelId=linkUrl.substring(0,linkUrl.indexOf("main"));
    		this.zoomChange='1';
    		var jsonObj = jQuery.parseJSON(allen.mapData["sub"+tunnelId]["sub"+tunnelId]);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/this.map/";
		    //allen.mapType='channelMain';
		    //new allen.showMap(globalJsonObj, "mapContainer",baseUrl); 
		    
		    $("#mapContainer").empty();
	        if (this.initMapInfo(globalJsonObj, "mapContainer",baseUrl)) {
	            this.startMap();
	        }
		    
    	} else {
    		globalUtil.addMask('mapContainer');
    		managerDialog = $.ligerDialog.waitting('正在更新' + iconTip + '数据，请稍后...');
    		allen.mapType='channelMain';
    		channelName = iconTip;
    		$(".pop-title-text").html(channelName);
    		$.post(global_param.context_name+"/getMapSumData.do",
		 			   {"tunnel_addr":linkUrl},
		 			   function(data){
		 				    if (data.dataError) {
		 				 	   globalUtil.warnMsg(iconTip + '数据出现异常');
		 					   managerDialog.close();
		 					   globalUtil.delMask('mapContainer');
		 					   return;
		 				    } 
		 				    managerDialog.close();
		 				    globalUtil.delMask('mapContainer');
		 				    $('#inputHidden').val(linkUrl);
		 				    $('#inputTunnelNameHidden').val(iconTip);
		 				    $('#inputHighwayHidden').val(highWayId);
		 				    $('#openIdViewSumEletric').html(data.elecsum+'千瓦时');
		 					$('#openIdViewSumSafeEletric').html(data.elec+'千瓦时');
		 					$('#openIdViewSumSafeCoal').html(data.coal+'千克');
		 					$('#openIdViewSumSafeCarbon').html(data.carbon+'千克');
		 					$('#openIdViewSumVehicle').html(data.flowrate+'辆');
		 					$('#openIdViewSumEquipAlarm').html(data.appearNum+'次');
		 					$('#openIdViewSumEquipRepair').html(data.resolveNum+'次');
		 					if (data.tunnelLeft == '1') {
		 						$('#openIdLeftCurrentSts').html('节能控制<img id="imageControl" src="../../images/win/realtime-safe-on.gif" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
		 					} else if (data.tunnelLeft == '2') {
		 						$('#openIdLeftCurrentSts').html('全开状态<img id="imageControl" src="../../images/win/realtime-all-on.gif" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
		 					} else {
		 						$('#openIdLeftCurrentSts').html('左洞连接服务器错误&nbsp;&nbsp;');
		 					}
		 					
		 					if (data.tunnelRight == '1') {
		 						$('#openIdRightCurrentSts').html('节能控制<img id="imageControl" src="../../images/win/realtime-safe-on.gif" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
		 					} else if (data.tunnelRight == '2') {
		 						$('#openIdRightCurrentSts').html('全开状态<img id="imageControl" src="../../images/win/realtime-all-on.gif" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
		 					} else {
		 						$('#openIdRightCurrentSts').html('右洞连接服务器错误&nbsp;&nbsp;');
		 					}

		 					$('#localWeather').html(data.weather);
		 					if (data.showNum == '2') {
		 						$('#localWeatherPicO').html('<img id="imageControl" src="'+data.gif1+'" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
			 					$('#localWeatherPicT').html('<img id="imageControl" src="'+data.gif2+'" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');	
		 					} else {
		 						$('#localWeatherPicO').html('<img id="imageControl" src="'+data.gif1+'" width="50" height="50" style="vertical-align:middle;"/>&nbsp;&nbsp;');
		 					}
		 					
		 					if(dialogObjForView){
		 		                dialogObjForView._setTitle(iconTip);
		 		                dialogObjForView.show();
		 		            } else{
		 		    		dialogObjForView = $.ligerDialog.open({
		 					target : $("#mapContainerForViewId"),
		 					title : '',
		 					width : 320,
		 					height : 360,
		 					title: iconTip,
		 					isResize : false,
		 					isDrag: false,
		 					modal:false,
		 					fixedType: true
		 				});
		 		     }
		 					
		 		});  
    		
    		
    }
    };
    
    showBMap.prototype.addTabOnMap = function(selectTunnelId) { 
    	if (selectTunnelId.indexOf("main")>0) {
    		this.tempPoint.x=null;
        	this.tempPoint.y=null; 
		    delete this.map.__listeners;
		    
    		var tunnelId=selectTunnelId.substring(0,selectTunnelId.indexOf("main"));
    		this.zoomChange='1';
    		var jsonObj = jQuery.parseJSON(allen.mapData["sub"+tunnelId]["sub"+tunnelId]);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/this.map/";
		    allen.mapType='channelMonitor';
		    new allen.showMap(globalJsonObj, "mapContainer",baseUrl); 
    	} else {
    	allen.mapType='channelMonitor';
    	var $ = window.parent.window.$;
    	var tab1;
    	tab1 = $("#framecenter").ligerGetTabManager();
        if (tab1.isTabItemExist('6501')) {
            tab1.removeTabItem('6501');
        }
    	tab1.addTabItem({tabid:'6501',text:'隧道设备', url: 'control/equipment_list.html?id='+selectTunnelId});
    	}
    	};
    	
    
    	showBMap.prototype.addDialogControlOnMap = function(iconTip,linkUrl) { 
    	if (linkUrl.indexOf("main")>0) {
    		this.tempPoint.x=null;
        	this.tempPoint.y=null;
        	delete this.map.__listeners;
        	
    		var tunnelId=linkUrl.substring(0,linkUrl.indexOf("main"));
    		this.zoomChange='1';
    		var jsonObj = jQuery.parseJSON(allen.mapData["sub"+tunnelId]["sub"+tunnelId]);
		     iconMark=jsonObj.mapArray[0].iconMark;
			 polyLine=jsonObj.mapArray[0].polyLine;
		   globalJsonObj = jsonObj;
		    var baseUrl = ""; 
		    allen.imageUrl="../../images/this.map/";
		    allen.mapType='realTimeMonitor';
		    new allen.showMap(globalJsonObj, "mapContainer",baseUrl);
	        
	        
    	} else {
    		allen.mapType='realTimeMonitor';
    		channelName = iconTip;
    		$(".pop-title-text").html(channelName);
    		getGridData(iconTip,linkUrl);
    		allen.controlTunnelId = linkUrl;
    	
    	}
  
    };
    var gridData;
    var getGridData = function(iconTip,linkUrl) { 
		globalUtil.addMask('mapContainer');
		manager = $.ligerDialog.waitting('正在更新' + iconTip + '数据，请稍后...');
    $.post(global_param.context_name+"/control/searchControlData.do",
			   {"tunnel_id_direction":linkUrl},
			   function(data){
				   gridData = data;
				   if(!dialogRealForView){
				   dialogRealForView = $("#maingrid").ligerGrid({
						// 设置属性
						columns : [ { display: '时间', name: 'sys_operate_time',width: '45%',  minWidth: 140, align: 'left'} ,
				                    { display: '操作', name: 'sys_operate_content',width: '30%', minWidth: 115},
				                    { display: '方向', name: 'tunnel_dirceion', width: '25%', minWidth: 80}
				                  ],
				                data: data,
								sortName : 'sys_operate_time',
								sortOrder : 'desc', //default is asc 
								width : '100%',
								height : '200',
								rownumbers : true,
								allowAdjustColWidth :false,
								heightDiff: -6,
								enabledSort: false,
								usePager:false
							});
		            }
				    	$("div.l-grid2").removeAttr("style"); 
				    	$("div.l-grid2").attr("style","width: 100%; left: 27px") ;
				    	
				    	$.post(global_param.context_name+"/control/searchRatioData.do",
				 			   {"tunnel_id_direction":linkUrl},
				 			   function(data){
				 				   if (data.tunnelLeft==1) {
				 					    $("#safeLeftId").removeAttr("checked"); 
				 						$("#allLeftId").removeAttr("checked"); 
				 						$("#safeLeftId").attr("checked","checked") ;
				 				        $("#imageLeftSafeControl").removeAttr("src");
				 				        $("#imageLeftSafeControl").attr("src", "../../images/win/realtime-safe-on.gif");
				 				        $("#imageLeftAllControl").removeAttr("src");
				 				        $("#imageLeftAllControl").attr("src", "../../images/win/realtime-all-off.gif");
				 				         	
				 				        	
				 				   } else if (data.tunnelLeft==2) {
				 					    $("#safeLeftId").removeAttr("checked"); 
				 						$("#allLeftId").removeAttr("checked"); 
				 						$("#allLeftId").attr("checked","checked") ;
				 						$("#imageLeftSafeControl").removeAttr("src");
			 				            $("#imageLeftSafeControl").attr("src", "../../images/win/realtime-safe-off.gif");
			 				            $("#imageLeftAllControl").removeAttr("src");
			 				            $("#imageLeftAllControl").attr("src", "../../images/win/realtime-all-on.gif");
				 				   } 
				 				   
				 				  if (data.tunnelRight==1) {
				 					    $("#safeRightId").removeAttr("checked"); 
				 						$("#allRightId").removeAttr("checked"); 
				 						$("#safeRightId").attr("checked","checked") ;
				 						$("#imageRightSafeControl").removeAttr("src");
			 				            $("#imageRightSafeControl").attr("src", "../../images/win/realtime-safe-on.gif");
			 				            $("#imageRightAllControl").removeAttr("src");
			 				            $("#imageRightAllControl").attr("src", "../../images/win/realtime-all-off.gif");
				 				   } else if (data.tunnelRight==2) {
				 					    $("#safeRightId").removeAttr("checked"); 
				 						$("#allRightId").removeAttr("checked"); 
				 						$("#allRightId").attr("checked","checked") ;
				 						$("#imageRightSafeControl").removeAttr("src");
			 				            $("#imageRightSafeControl").attr("src", "../../images/win/realtime-safe-off.gif");
			 				            $("#imageRightAllControl").removeAttr("src");
			 				            $("#imageRightAllControl").attr("src", "../../images/win/realtime-all-on.gif");
				 				   } 
				 				  
				 				 if ((data.tunnelLeft==1 
				 						 || data.tunnelLeft==2)
				 						 && (data.tunnelRight==1
				 						 || data.tunnelRight==2)) {
				 					 
				 					if(dialogRealForView){
				 					   dialogRealForView.loadData(gridData); 
				 		            }
				 					 
				 					if(allen.dialogControl){
				 						//allen.dialogControl.hide();
				 						allen.dialogControl._setTitle(iconTip);
				 						allen.dialogControl.show();
				 		            } else{
			 			    		allen.dialogControl = $.ligerDialog.open({
			 							target : $("#controlStsDivId"),
			 							title :  '',
			 							width : 400,
			 							height : 375,
			 							isResize : false,
			 							isDrag: false,
			 							title: iconTip,
			 							modal:false,
			 							fixedType: true
			 						});
				 		            }
				 					manager.close();
				 					globalUtil.delMask('mapContainer');
				 				   } else {
				 					  manager.close();
					 				  globalUtil.delMask('mapContainer');
				 					  $.ligerDialog.warn('数据出现异常，请检查网络与设备，并联系管理员！');
				 				   }   				 
				 		});    	
		});
    };

/*
     mark and show some polyline on the this.map
    */
    showBMap.prototype.addPolilineOnMap = function() {
        if (!this.curPoint.polyLine || this.curPoint.polyLine.length === 0) {
            return;
        }
        
        var polyLinegroup = this.curPoint.polyLine;
        var polyObj = null;
        var lineArray = null;
        var marker = null;
        var label = null;
        var myicon = null;
         myicon = new BMap.Icon(allen.imageUrl+"line_mark.png", new BMap.Size(48, 48), {
                anchor: new BMap.Size(10, 48)
            });
        
        var  myico = new BMap.Icon(allen.imageUrl+"flag.png", new BMap.Size(0, 0), {
            anchor: new BMap.Size(0, 0)
        });  
        
        var i, k;
        for (i = 0; i < polyLinegroup.length; i++) {
            polyObj = polyLinegroup[i];

            if (!polyObj.polyLinePoint || polyObj.polyLinePoint.length === 0) {
                break;
            } else {
                // paint the polyline on the this.map
                lineArray = [];
                for (k = 0; k < polyObj.polyLinePoint.length; k++) {
                    lineArray.push(new BMap.Point(polyObj.polyLinePoint[k].x, polyObj.polyLinePoint[k].y));
                }
                this.map.addOverlay(new BMap.Polyline(lineArray, {
                    strokeColor: polyObj.polyLineColor,
                    strokeWeight: 8,
                    strokeOpacity: 0.5
                }));
                
                this.map.addOverlay(new BMap.Polyline(lineArray, {
                    strokeColor: "#ffffff",
                    strokeWeight: 2
                }));

                // put the marker on the this.map
                if (polyObj.events && polyObj.events.length > 0) {
                            	 marker = new BMap.Marker(new BMap.Point(polyObj.polyLinePoint[0].x, polyObj.polyLinePoint[0].y),{
                                     icon: myicon
                                 });
                } else {
                	 marker = new BMap.Marker(new BMap.Point(polyObj.polyLinePoint[0].x, polyObj.polyLinePoint[0].y),{
                         icon: myico
                     });
                }
                 
                
                marker.polyobj = polyObj;
                
                if (polyObj.polyLineTitle && polyObj.polyLineTitle.length > 0) {
                    label = new BMap.Label(polyObj.polyLineTitle, {
                        offset: {
                            width: -30,
                            height: 50
                        }
                    });
                    label.setStyle({
                        border: "0",
                        backgroundColor: "",
                        color: polyObj.polyLineTitleColor || "#ffff00",
                        fontWeight: "bolder",
                        fontSize: "small"
                    });
                    marker.setLabel(label);
                }

                // add an info tip for the marker                
                if (polyObj.events && polyObj.events.length > 0) {
                    for(var j=0; j<polyObj.events.length; j++){
                        if(polyObj.events[j]==="click"){
                            if(polyObj.iconLink){
                                marker.addEventListener("click", function() {
                                    stopShow();
                                    this.map.closeInfoWindow();
                                    var urlPath = url + this.polyobj.iconLink.linkUrl;
                                    var openWidth = this.polyobj.iconLink.openWidth || 800;
                                    var openHeight = this.polyobj.iconLink.openHeight || 600;
                                    var showName = this.polyobj.iconLink.linkName || "";
                                    $.pdialog.open(urlPath, this.polyobj.iconLink.linkUrl, showName,{width:openWidth,height:openHeight,max:false,mask:true,mixable:true, minable:true,resizable:true,drawable:true,fresh:true,close:"", param:""});
                                });
                            }
                        }else if(polyObj.events[j]==="mouseover"){

                            if (polyObj.polyLineTip && polyObj.polyLineTip.length > 0) {
                                marker.addEventListener("mouseover", function() {
                                   /* this.map.closeInfoWindow();
                                    this.openInfoWindow(new BMap.InfoWindow(this.polyobj.polyLineTip, {
                                        width: 360,
                                        height:200,
                                        enableAutoPan: false
                                    })); */
                                    $.ligerDialog.open({
                						target : $("#openDialogForViewId"),
                						title : dialogTitle || '',
                						width : 600,
                						height : 500,
                						isResize : false
                					});
                                    
                                });
                            }                        
                        }
                    }
                }
                
                this.map.addOverlay(marker);

            }
        }
    };

/*
     define the legend control
    */
    var LegendControl = function() {
        this.defaultAnchor = BMAP_ANCHOR_BOTTOM_LEFT;
        this.defaultOffset = new BMap.Size(10, 40);
    };
    LegendControl.prototype = new BMap.Control();
    LegendControl.prototype.initialize = function() {
        var div;
        if (document.getElementById("map_legendDiv")) {
            div = document.getElementById("map_legendDiv");
        } else {
            div = document.createElement("div");
            div.id = "map_legendDiv";
            div.className = "map_legendDiv";
            this.map.getContainer().appendChild(div);
        }
        $('#map_legendDiv').html("");
        return div;
    };

/*
     define the title bar control
    */
    var TitleControl = function() {
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(0, 10);
    };
    TitleControl.prototype = new BMap.Control();
    TitleControl.prototype.initialize = function() {
        var div;
        if (document.getElementById("map_titleDiv")) {
            div = document.getElementById("map_titleDiv");
        } else {
            div = document.createElement("div");
            div.id = "map_titleDiv";
            div.className = "map_titleDiv";
            this.map.getContainer().appendChild(div);
        }
        $('#map_titleDiv').html("");
        return div;
    };

/*
     define the project description control
    */
    var ContentControl = function() {
        this.defaultAnchor = BMAP_ANCHOR_TOP_LEFT;
        this.defaultOffset = new BMap.Size(50, 70);
    };
    ContentControl.prototype = new BMap.Control();
    ContentControl.prototype.initialize = function() {
        var div;
        if (document.getElementById("map_contentTopDiv")) {
            div = document.getElementById("map_contentTopDiv");
        } else {
            div = document.createElement("div");
            div.id = "map_contentTopDiv";
            div.className = "map_contentTopDiv";
            this.map.getContainer().appendChild(div);
        }
        $('#map_contentTopDiv').html("");
        return div;
    };

/*
     define the play controler 
    */
    var PlayControl = function() {
        this.defaultAnchor = BMAP_ANCHOR_BOTTOM_RIGHT;
        this.defaultOffset = new BMap.Size(10, 10);
    };
    PlayControl.prototype = new BMap.Control();
    PlayControl.prototype.initialize = function() {
        var div = document.createElement("div");
        div.id = "map_playControlDiv";
        div.className = "map_playControlDiv";

        var imgplay = document.createElement("img");
        imgplay.id = "map_imgplay";
        imgplay.setAttribute("src", allen.imageUrl+"play.png");
        imgplay.setAttribute("width", "50");
        div.appendChild(imgplay);

        var imgstop = document.createElement("img");
        imgstop.id = "map_imgstop";
        imgstop.setAttribute("src", allen.imageUrl+"pause.png");
        imgstop.setAttribute("width", "50");
        div.appendChild(imgstop);
        this.map.getContainer().appendChild(div);

        $("#map_imgplay").click(function() {
            if (this.mapLoaded) {
                stopShow();
                loadMap();
                $("#map_imgstop").attr("src", allen.imageUrl+"pause.png");
            }
        });

        $("#map_imgstop").click(function() {
            if (this.mapLoaded) {
                stopShow('this.map');
                $("#map_imgstop").attr("src", allen.imageUrl+"stop.png");
            }
        });

        if (this.circleFlag === 2) {
            $("#map_playControlDiv").hide();
        }
        return div;
    };

})();