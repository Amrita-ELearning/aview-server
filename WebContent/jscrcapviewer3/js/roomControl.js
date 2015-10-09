/*
 * Filename		: joinControl.js
 * Description	: Contains scripts that control the form.
 * Copyright	: 321Meet.com � 2010.
 */

// Global Variables
var minWidth = 1;
var minHeight = 1;
var origValue = [];
var origColor = [];
var emailRegEx = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i;
var lastSetVolume = 100;
var volumeMute = false;
var sliderBoxVisible = false;
var os = "";

$(document).ready( function() {
		//$('#bottomBar .logoContainer .logo a').attr( "href", getBaseURL() );		
		
		// Set height of sidebar.
		resizeSidebar();
		resizePanes();
		
		$(window).resize( function() {
			resizeSidebar();
			resizePanes();
		});
	
		/* BUTTONS */
		
		$('#bottomBar .btnFullScreen').bind({
			click: function() {
				document.flashPlayer.ShowFullScreen();
			}
		});
		
		$('#bottomBar .btnPanScreen').bind({
			click: function() {
				//isAutoTrack = !isAutoTrack;
				//if(isAutoTrack)
					document.flashPlayer.ToggleServerPanning();
				//else
					//document.flashPlayer.SetAutoTrack('false');
			}
		});
		
		$('#bottomBar .btnRemoteControl').bind({
			click: function() {
				if (document.flashPlayer.GetRemoteControlStatus())
					document.flashPlayer.EndRemoteControl();
				else
					document.flashPlayer.StartRemoteControl();
			}
		});
		
		$('#bottomBar .btnStopSharing').bind({
			click: function() {
				stopJScrCap();
				sendEvent(8, '');
				isSharing = false;
			}
		});
		
		$('#bottomBar #audioBtn').bind({
			click: function(event) {
				event.stopPropagation();

				$("#bottomBar #sliderBox").show();
				
				sliderBoxVisible = true;
			}
		}); 
		
		$('#bottomBar #setMute').bind({
			click: function() {
				// Set sound to on and off
				if (!volumeMute) {
					setStreamVolume(0);
					
					$('#bottomBar .btnMute').addClass( "btnUnmute" );
					
					volumeMute = true;
				} else { 
					setStreamVolume(lastSetVolume);
					
					$('#bottomBar .btnMute').removeClass( "btnUnmute" );
					
					volumeMute = false;
				}
			}
		}); 
		
	/* Slider Volume Control */
	$( "#sliderVolume" ).slider({
		orientation: "vertical",
		range: "min",
		min: 0,
		max: 100,
		value: 100,
		slide: function( event, ui ) {
			//$( "#amount" ).val( ui.value );
			lastSetVolume = ui.value;
			if (!volumeMute)
				setStreamVolume(ui.value);
		}
	});
	
	/* Add click event to the Body */
	$("body").bind({ 
		click: function() { 
			if ( sliderBoxVisible ) { 
				$("#bottomBar #sliderBox").hide();
				
				sliderBoxVisible = false;
			}
		}
	});
	/* Prevent event propagation */
	$("#sliderBox").bind({ 
		click: function(event) { 
			event.stopPropagation();
		}
	});
	
	if (displayRemoteControl == "1" || displayRemoteControl == "true")
		$(".btnRemoteControl").show();
	
	os = BrowserDetect.OS;
	initStream();
});

 
	// Resize viewer panes.
	function resizePanes() {
		// If window height is less the min height, set the minimum height
		if ( $(window).height() > minHeight ) {
			var hWindowPane = $(window).height();
		} else {
			var hWindowPane = minHeight;
		}
		
		// If window width is less than min width, set minimum width.
		if ( $(window).width() > minWidth ) {
			var wWindowPane = $(window).width();
		} else {
			var wWindowPane = minWidth;
		}
		
		// Set function variables.
		var hFooterBar = $('#bottomBar').height();
		var wSidebar = parseInt( $('#widget').css( "width" ) );
		
		// Set width of panes.
		$('#paneFull').height( hWindowPane - hFooterBar );
		$('#paneSidebar').height( hWindowPane - hFooterBar );
		
		// Set height of panes.
		$('#paneFull').width( wWindowPane );
		$('#paneSidebar').width( wWindowPane );
		
		// Vertically center.
		var hCenter = $('.center').height();
		var marginOffset = ( ( hWindowPane - hFooterBar ) / 2 ) - ( hCenter / 2 );
		
		if ( marginOffset < 0 ) {
			marginOffset = 5;
		}
		
		//$('#paneFull .center').css( "margin-top", marginOffset );
		//$('#paneSidebar .center').css( "margin-top", marginOffset - 60 );
	}
 
	// Resize height of sidebar.
	function resizeSidebar() {
		// Set height of container.
		$('body').height( $(window).height() );
		
		// If window height is less the min height, set the minimum height
		if ( $(window).height() > minHeight ) {
			var hWindowPane = $(window).height();
		} else {
			var hWindowPane = minHeight;
		}
		
		// Set function variables.
		var hPeopleContent = parseInt( $('#widgetContent #peopleContent .innerMiddle').css( "height" ) );
		var hChatContent = parseInt( $('#widgetContent #chatContent .innerMiddle').css( "height" ) );
		var hSidebar = $('#widget').height();
		var hFooterBar = $('#bottomBar').height();
		
		// Computer for horizontal space difference.
		var hSpace = hWindowPane - hFooterBar - hSidebar;
		
		// Set height of middle containers.
		$('#widgetContent #peopleContent .innerMiddle').height( hPeopleContent + hSpace );
		$('#widgetContent #chatContent .innerMiddle').height( hChatContent + hSpace );
		
		// Set height of middle container elements.
		$('#widgetContent #peopleContent .innerMiddle .participants').height( hPeopleContent + hSpace );
		$('#widgetContent #chatContent .innerMiddle .chatModule').height( hChatContent + hSpace );
	}
	
	// Get Base URL
	function getBaseURL() {
		var url = location.href;  // entire url including querystring - also: window.location.href;
		var baseURL = url.substring(0, url.indexOf('/', 14));

		if (baseURL.indexOf('http://localhost') != -1) {
			// Base Url for localhost
			var url = location.href;  // window.location.href;
			var pathname = location.pathname;  // window.location.pathname;
			var index1 = url.indexOf(pathname);
			var index2 = url.indexOf("/", index1 + 1);
			var baseLocalUrl = url.substr(0, index2);

			return baseLocalUrl + "/";
		}
		else {
			// Root Url for domain name
			return baseURL + "/";
		}
	}
	
	// Get Client Time
	function getTime() {
		// Set variables.
		var a_p = "";
		var d = new Date();
		
		// Get current client hour.
		var curr_hour = d.getHours();
		
		// Set AM or PM.
		if (curr_hour < 12)
		{
			a_p = "AM";
		} else
		{
			a_p = "PM";
		}
		
		// Format the hour output.
		if (curr_hour == 0)
		{
			curr_hour = 12;
		}
		
		if (curr_hour > 12)
		{
			curr_hour = curr_hour - 12;
		}
		
		if ( curr_hour < 10 )
		{
			curr_hour = "0" + curr_hour;
		}

		// Get current client minute.
		var curr_min = d.getMinutes();
		
		// Format the minute output.
		if ( curr_min < 10 )
		{
			curr_min = "0" + curr_min;
		}
		
		// Return formatted time.
		return curr_hour + ":" + curr_min + " " + a_p;
	}
	
	
	// Returns vertical center marginal offset of the object.
	function vCenter( obj ) {
		var hWindowPane = $(window).height();								
		var hCenter = $(obj).height() + parseInt( $(obj).css( "padding-top" ) ) + parseInt( $(obj).css( "padding-bottom" ) );

		// Vertically center.
		var marginOffset = ( hWindowPane / 2 ) - ( hCenter / 2 );
		
		if ( marginOffset < 0 ) {
			marginOffset = 5;
		}
		
		return marginOffset;
	}
	
	function setStreamVolume(vol) { 
		document.flashPlayer.SetVolume(vol);
	}
	function querySt(ji) {
		hu = window.location.search.substring(1);
		gy = hu.split("&");
		if (ji) {
			for (i = 0; i < gy.length; i++) {
				ft = gy[i].split("=");
				if (ft[0].toLowerCase() == ji.toLowerCase()) {
					return ft[1];
				}
			}
		}
		else {
			ft = new Array(gy.length);
			for (i = 0; i < gy.length; i++) {
				ft[i] = gy[i].split("=");
			}
			return ft;
		}
	}
	
	var streamName = querySt("id") || "";
	//var rtmp = querySt("url") || "rtmp://204.236.225.74/splitstream";
	var rtmp = querySt("url") || "rtmp://184.73.1.126/splitstream";
	//var rtmp = querySt("url") || "rtmp://46.137.13.189/live";
	var buffer;
	var remote = querySt("control") || "0";
	var displayRemoteControl = querySt("advcontrol") || "0";
	var fitFiltered = querySt("fitfiltered") || "0";
	function initStream() { 
		buffer = querySt("buffer") || "0"; 
		isAutoTrack = false;
		var flashvars = {
			vidName: streamName,
			serverUrl: rtmp,
			bufferTime: buffer,
			events: 1,
			debug: 1,
			forceShowVid: 1,
			fitFiltered: fitFiltered,
			playerID: "flashPlayer",
			playerContainerID: "paneSidebar"
		};
		
		var params = {
			allowFullScreen: 'true',
			allowScriptAccess: 'always',
			wmode: 'opaque'
		};
		var attributes = {};
		
		swfobject.embedSWF('swf/SMLPlayer.swf?' + getRandomUserColor(), "flashPlayer", '100%', '100%', "9.0.0", false, flashvars, params, attributes);
		setTimeout('initPen();', 5000);
		//LoDRightClickTrap.init("flashPlayer","paneSidebar");
		if ( (remote == "1" || remote == "true") && (displayRemoteControl == "1" || displayRemoteControl == "true") )
			initRemote();
	}
	
	function getRandomUserColor()
	{
		var c = Math.random()*0xFFFFFF<<0;
		if (c==0) return getRandomUserColor();
		var h = c.toString(16);
		while(h.length<6){h="0"+h;} return h;
	}
	
	function initPen()
	{
		try
		{
			document.flashPlayer.setUserInfo(getRandomUserColor());
			//document.flashPlayer.laserPenConnect(streamName);
		}
		catch(err)
		{
			setTimeout('initPen();', 5000);
		}
	}
	
	function initRemote()
	{
		try
		{
			document.flashPlayer.StartRemoteControl();
		}
		catch(err)
		{
			setTimeout('initRemote();', 2000);
		}
	}

	function SMLPlayerEvent(event, code, description)
	{
		//alert(event + " " + code + " " + description);
		switch(event)
		{
			case 'Player':
			{
				switch (code)
				{
					case 'RemoteControl.Activate.Complete':
						$('.btnRemoteControl').addClass("btnRemoteControlActive");
						break;
					case 'RemoteControl.Deactivate.Complete':
						$('.btnRemoteControl').removeClass("btnRemoteControlActive");
						break;
					case 'ServerPan.Activate.Complete':
						$('.btnPanScreen').addClass("btnPanScreenActive");
						break;
					case 'ServerPan.Deactivate.Complete':
						$('.btnPanScreen').removeClass("btnPanScreenActive");
						break;
					case 'SetVolume.Complete':
						$( "#sliderVolume" ).slider( "option", "value", parseFloat(description)*100 );
						break;
				}
			}
			case 'stream':
			//if(code == 'unpublished')
			//window.location = 'http://<?php echo $_SERVER['HTTP_HOST']; ?>/CMS/';
				break;
			default:
				break;
		}
	}