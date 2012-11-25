<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div class="content">
		
		<div class="mappy" style="overflow: hidden; width: 600px; height: 400px; position: relative; cursor: hand; z-index: 15; opacity: 1;">
			
			<div id="mapCover" style="position: absolute; top: 0px; left: 0px; width: 600px; height: 400px; z-index: 5; opacity: ;"></div>			
			<div id="zoomInBtn" style="position: absolute; top: 5px; left: 5px; width: 20px; height: 20px; background: green; z-index: 10; "></div>
			<div id="zoomOutBtn" style="position: absolute; top: 30px; left: 5px; width: 20px; height: 20px; background: green; z-index: 10; "></div>
			<!--
				<div id="zoomInBtn" style="position: absolute; top: 5px; left: 5px; width: 20px; height: 20px; background: grey; z-index: -5; text-align: center; vertical-align: center;">+</div>
			</div>
			-->
			<!--

			<div id="zoomOutBtn">-</div>
			-->
			
			<img class="mapElement" id="img0" />
			<img class="mapElement" id="img1" />
			<img class="mapElement" id="img2" />
			<img class="mapElement" id="img3" />
			<img class="mapElement" id="img4" />
			<img class="mapElement" id="img5" />

			<img class="mapElement" id="img6" />
			<img class="mapElement" id="img7" />
			<img class="mapElement" id="img8" />
			<img class="mapElement" id="img9" />
			<img class="mapElement" id="img10" />
			<img class="mapElement" id="img11" />		

			<img class="mapElement" id="img12" />
			<img class="mapElement" id="img13" />
			<img class="mapElement" id="img14" />
			<img class="mapElement" id="img15" />
			<img class="mapElement" id="img16"/>
			<img class="mapElement" id="img17" />	

			<img class="mapElement" id="img18" />
			<img class="mapElement" id="img19" />
			<img class="mapElement" id="img20" />
			<img class="mapElement" id="img21" />
			<img class="mapElement" id="img22" />
			<img class="mapElement" id="img23" />
		
			<img class="mapElement" id="img24" />
			<img class="mapElement" id="img25" />
			<img class="mapElement" id="img26" />
			<img class="mapElement" id="img27" />
			<img class="mapElement" id="img28" />
			<img class="mapElement" id="img29" />	

			<!--
			<div id="zoomInCover"></div>
			-->


		</div>

		<div id="info" style="position: absolute; left: 10px; top: 10px; width: 300px; height: 150px; background: #ccc; z-index: -20; padding: 5px;" ></div>
	</div>
		
		<script>

			function onMousePress(e) {
				
				mouseState.button = 1;
				mouseState.clicked.position.x = e.pageX;
				mouseState.clicked.position.y = e.pageY;
				mouseState.clicked.mapViewCorner.x = mapView.corner.x;
				mouseState.clicked.mapViewCorner.y = mapView.corner.y;
				mouseState.clicked.moved.x = 0;
				mouseState.clicked.moved.y = 0;
				showInfo();
			}
			
			function onMouseMove(e) {
				
				if (mouseState.button == 1) {
					mouseState.clicked.moved.x = e.pageX - mouseState.clicked.position.x;
					mouseState.clicked.moved.y = e.pageY - mouseState.clicked.position.y;
					mapView.corner.x = mouseState.clicked.mapViewCorner.x + mouseState.clicked.moved.x;
					mapView.corner.y = mouseState.clicked.mapViewCorner.y + mouseState.clicked.moved.y;
					setImagesPositions();
					if (mapView.corner.y < -2 * mapView.area.height) {
						moveTopRow();
					}
					if (mapView.corner.y > -1 * mapView.area.height) {
						moveBottomRow();
					}
					if (mapView.corner.x < -2 * mapView.area.width) {
						moveLeftColumn();
					}
					if (mapView.corner.x > -1 * mapView.area.width) {
						moveRightColumn();					
					}
				}
				var bottomLeft = new Object();
				bottomLeft.x = viewPort.corner.x + mapView.corner.x;
				bottomLeft.y = viewPort.corner.y + mapView.corner.y + mapView.area.height * mapView.height;
				var longitudePerPixel = mapModel.area.width / mapView.area.width;
				var latitudePerPixel = mapModel.area.height / mapView.area.height;				
				
				mouseState.coords.longitude = Math.round((mapModel.corner.x * mapModel.area.width + (e.pageX - bottomLeft.x) * longitudePerPixel) * 10000) / 10000;
				mouseState.coords.latitude = Math.round((mapModel.corner.y * mapModel.area.height + (bottomLeft.y - e.pageY) * latitudePerPixel) * 10000) / 10000;
				showInfo();
			}
			
			function onMouseRelease(e) {
				
				mouseState.button = 0;
			}
			
			function init() {
			
				doingJob = 0;
				
				mapModel = new Object();
				
				
				// Zoom level
				mapModel.zoomLevel = new Object();
				mapModel.zoomLevel.current = 4;
				mapModel.zoomLevel.min = 1;
				mapModel.zoomLevel.max = 9;
			
				// Index of map fragment shown in the left bottom corner of mapView
				mapModel.corner = new Object();			
				mapModel.corner.x = 17;
				mapModel.corner.y = 103;

				// Dimensions of one area in degrees
				mapModel.area = new Object();
				mapModel.area.width = 1;
				mapModel.area.height = 0.5;
				
			
				mapView = new Object();

				// Size of the picture in pixels
				mapView.area = new Object();
				mapView.area.width = 200;
				mapView.area.height = 200;
			
				// Location of left top corner of map in relation to viewPort.corner
				mapView.corner = new Object();
				mapView.corner.x = -mapView.area.width * 1.5;
				mapView.corner.y = -mapView.area.height * 1.5;
			
				// Size of the grid of images
				mapView.width = 6;
				mapView.height = 5;
						
			
				viewPort = new Object();
				// Absolute location of left top corner of viewPort
				// May be set by calling setViewPortAbsolutePosition() function
				viewPort.corner = new Object();

				mouseState = new Object();
				mouseState.button = 0;
				mouseState.clicked = new Object();
				mouseState.clicked.position = new Object();
				mouseState.clicked.position.x = 0;
				mouseState.clicked.position.y = 0;
				mouseState.clicked.mapViewCorner = new Object();
				mouseState.clicked.mapViewCorner.x = 0;
				mouseState.clicked.mapViewCorner.y = 0;
				mouseState.clicked.moved = new Object();
				mouseState.clicked.moved.x = 0;
				mouseState.clicked.moved.y = 0;
				mouseState.coords = new Object();
				mouseState.coords.latitude = 0;
				mouseState.coords.longitude = 0;			
			
				// Initialize mappings 
				mapView.mapping = new Array(mapView.width);
				for (var x = 0; x < mapView.width; x++) {
					mapView.mapping[x] = new Array(mapView.height);
					for (var y = 0; y < mapView.height; y++) {
						var imgId = 'img' + (x + mapView.width * y);
						var img = document.getElementById(imgId);
						mapView.mapping[x][y] = img;
					}
				}
				setImagesPositions();
				setViewPortAbsolutePosition();
				setImagesSources();
			}
			
			
			function requestZoomChange(newZoom) {
				var url = 'http://localhost/TrackerServer/map/area/info/' + newZoom + '.json';				
				$.getJSON(
					url,
					function(data) {	
						if (data.level == 0) return;						
						var centerCoords = new Object();
						centerCoords.x = mapModel.corner.x * mapModel.area.width + (mapView.width / 2) * mapModel.area.width;
						centerCoords.y = mapModel.corner.y * mapModel.area.height + (mapView.height / 2) * mapModel.area.height;							
						mapModel.zoomLevel.current = data.level;
						mapModel.area.width = data.width;
						mapModel.area.height = data.height;
						mapModel.corner.x  = Math.round((centerCoords.x - (mapView.width / 2) * mapModel.area.width) / mapModel.area.width);
						mapModel.corner.y = Math.round((centerCoords.y - (mapView.height / 2) * mapModel.area.height) / mapModel.area.height);						
						setImagesPositions();
						setViewPortAbsolutePosition();
						setImagesSources();
					}
					);
					
			}			
			
			/**
			 * Finds absolute position of layer 'mapCover' and sets viewPort.corner variable value
			 **/
			function setViewPortAbsolutePosition() {
				
				var element = document.getElementById('mapCover');
				if (element.getBoundingClientRect) {
					var viewportElement = document.documentElement;  
					var box = element.getBoundingClientRect();
					var scrollLeft = viewportElement.scrollLeft;
					var scrollTop = viewportElement.scrollTop;
					viewPort.corner.x = Math.round(box.left + scrollLeft);
					viewPort.corner.y = Math.round(box.top + scrollTop);
				}
			}
			
			/**
			 * Sets image position for given x, y in mapping table
			 */
			function setImagePosition(x, y) {
				var posX = mapView.corner.x + x * (mapView.area.width - 0);
				var posY = mapView.corner.y + (mapView.height - y - 1) * (mapView.area.height - 0);
				var image = mapView.mapping[x][y];
				image.style.left = posX;
				image.style.top = posY;
			}
			function setImagesPositions() {
				if (doingJob == 1) return;
				doingJob = 1;
				for (var x = 0; x < mapView.width; x++) {
					for (var y = 0; y < mapView.height; y++) {
						setImagePosition(x, y);
					}
				}
				doingJob = 0;
			}
			
			/** 
			 * Sets source of the image mapped by given x and y
			 **/
			function setImageSource(x, y) {
				var image = mapView.mapping[x][y];
				image.src = 'http://localhost/TrackerServer/map/area/' + mapModel.zoomLevel.current + '/' + (mapModel.corner.x + x) + '/' + (mapModel.corner.y + y) + '.img';				
			}
			function setImagesSources() {
				for (var x = 0; x < mapView.width; x++) {
					for (var y = 0; y < mapView.height; y++) {
						setImageSource(x, y);
					}
				}
			}
			
			/**
			 * Shows debug information
			 **/
			function showInfo() {
				text = '';
				text = text + 'viewPort.corner=(' + viewPort.corner.x + ',' + viewPort.corner.y + ')<br/> ';
				text = text + 'mouseState.button=' + mouseState.button + ';<br/>';
				text = text + 'mouseState.clicked.position=(' + mouseState.clicked.position.x + ',' + mouseState.clicked.position.y + ');<br/>';
				text = text + 'mouseState.clicked.moved=(' + mouseState.clicked.moved.x + ',' + mouseState.clicked.moved.y + ');<br/>';
				text = text + 'mapModel.corner=(' + mapModel.corner.x + ',' + mapModel.corner.y + ');<br/>';
				text = text + 'mapView.corner=(' + mapView.corner.x + ',' + mapView.corner.y + ');<br/>';
				text = text + 'mouseState.coords=(' + mouseState.coords.latitude + ',' + mouseState.coords.longitude + ');<br/>';
				text = text + 'mapModel.zoomLevel.current=' + mapModel.zoomLevel.current + ';<br/>';
				element = document.getElementById('info');
				element.innerHTML = text;
			}
			
			function moveTopRow() {
							
				mouseState.clicked.mapViewCorner.y = mouseState.clicked.mapViewCorner.y + mapView.area.height;
				mapView.corner.y = mapView.corner.y + mapView.area.height;
				mapModel.corner.y = mapModel.corner.y - 1;				
				for (var x = 0; x < mapView.width; x++) {
					var val = mapView.mapping[x][mapView.height - 1];
					for (var y = mapView.height - 1; y > 0; y--) {
						mapView.mapping[x][y] = mapView.mapping[x][y - 1];
					}
					mapView.mapping[x][0] = val;
				}
				for (var x = 0; x < mapView.width; x++) {
					setImageSource(x, 0);
				}
			}
			
			function moveBottomRow() {
			
				mouseState.clicked.mapViewCorner.y = mouseState.clicked.mapViewCorner.y - mapView.area.height;
				mapView.corner.y = mapView.corner.y - mapView.area.height;
				mapModel.corner.y = mapModel.corner.y + 1;
				for (var x = 0; x < mapView.width; x++) {
					var val = mapView.mapping[x][0];
					for (var y = 0; y < mapView.height - 1; y++) {
						mapView.mapping[x][y] = mapView.mapping[x][y + 1];
					}
					mapView.mapping[x][mapView.height - 1] = val;
				}
				for (var x = 0; x < mapView.width; x++) {
					setImageSource(x, mapView.height - 1);
				}
			}
			
			function moveLeftColumn() {
				
				mouseState.clicked.mapViewCorner.x = mouseState.clicked.mapViewCorner.x + mapView.area.width;
				mapView.corner.x = mapView.corner.x + mapView.area.width;
				mapModel.corner.x = mapModel.corner.x + 1;
				
				var leftColumn = mapView.mapping[0];
				for (var x = 0; x < mapView.width - 1; x++) {
					mapView.mapping[x] = mapView.mapping[x + 1];
				}
				mapView.mapping[mapView.width - 1] = leftColumn;
				for (var y = 0; y < mapView.height; y++) {
					setImageSource(mapView.width - 1, y);
				}
			}
			
			function moveRightColumn() {
				
				mouseState.clicked.mapViewCorner.x = mouseState.clicked.mapViewCorner.x - mapView.area.width;
				mapView.corner.x = mapView.corner.x - mapView.area.width;
				mapModel.corner.x = mapModel.corner.x - 1;
				
				var rightColumn = mapView.mapping[mapView.width - 1];
				for (var x = mapView.width - 1; x > 0; x--) {
					mapView.mapping[x] = mapView.mapping[x - 1];
				}
				mapView.mapping[0] = rightColumn;
				for (var y = 0; y < mapView.height; y++) {
					setImageSource(0, y);
				}
			}			
		
			function onBodyLoad() {
				init();
				$(window).resize(function() { onWindowResize(); });
				$(document).mousemove(function(e) { onMouseMove(e); });
		
				$("#mapCover").mousedown(function(e) { onMousePress(e); });
				$(document).mouseup(function(e) { onMouseRelease(e); });
				$("#zoomInBtn").mousedown(function() { requestZoomChange(mapModel.zoomLevel.current + 1); });
				$("#zoomOutBtn").mousedown(function() { requestZoomChange(mapModel.zoomLevel.current - 1); });
			}
		
			function onWindowResize() {
				setViewPortAbsolutePosition();
				showInfo();
			}

		
			$(document).ready(function() { onBodyLoad();});

			
			//$("#mapCover").mouseleave(function(e) { doMouseUp(e); });
		
		</script>
	
	