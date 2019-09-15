$(document).ready(function() {
    $(".fancybox").fancybox({
		'autoDimensions' : false,
		'padding' : 0,
		'margin' : 100,
		'modal' : false,
		'width' : 560,
		'height' : 340,
		'transitionIn' : 'fade',
		'transitionOut' : 'fade',
		'cyclic' : 'true',
		helpers: {
			 title: {
		            type: 'inside',
		            position: 'top'
		        },
	        buttons: {
	            tpl: '<div id="fancybox-buttons"><ul><li><a class="btnPrev" title="Previous" href="javascript:;"></a></li><li><a class="btnPlay" title="Start slideshow" href="javascript:;"></a></li><li><a class="btnNext" title="Next" href="javascript:;"></a></li><li><a class="btnToggle" title="Toggle size" href="javascript:;"></a></li><li><a class="btnClose" title="Close" href="javascript:jQuery.fancybox.close();"></a></li>' +
	            '<li><a class="btnLeftRotate" title="Left Rotate" href="javascript:;">-90</a></li><li><a class="btnRightRotate" title="Right Rotate" href="javascript:;">+90</a></li></ul></div>',
	            afterShow: function (opts, obj) {
	            }
	        }
	
		},
	    afterShow: function (opts, obj) { 
	   	 //alert();
	    },
	    closeBtn: true, // you will use the buttons now
	    arrows: true
    });
    
    $('body').on('click','.btnLeftRotate', function() {

        rotateLeft();
    })

    $('body').on('click','.btnRightRotate', function() {
        rotateRight();
    })
    
    var rad = 0;
    function rotateLeft() {
        rad = rad - 90;
        if (rad == -360) {
            rad = 0;
        }
        $(".fancybox-wrap").rotate(rad);
    }

    function rotateRight() {
        rad = rad + 90;
        if (rad == 360) {
            rad = 0;
        }
        $(".fancybox-wrap").rotate(rad);
    }
    
    $('.fancyboxPdf').fancybox({
    	type: 'html',
    	autoSize: false,
    	content: '<embed src="'+'http://localhost:8080/BTCL_Automation/DownloadFile.do?documentID=128001'+'#nameddest=self&page=1&view=FitH,0&zoom=80,0,0" type="application/pdf" height="99%" width="100%" />',
    	beforeClose: function() {
    		$(".fancybox-inner").unwrap();
    	}
    });
})