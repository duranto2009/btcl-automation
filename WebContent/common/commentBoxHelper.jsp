<link href="${context}assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.css" rel="stylesheet" type="text/css" />

<script src="${context}assets/global/plugins/bootstrap-wysihtml5/wysihtml5-0.3.0.js" type="text/javascript"></script>
<script src="${context}assets/global/plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.js" type="text/javascript"></script>
<script type="text/javascript">
var initWysihtml5 = function () {
    $('.inbox-wysihtml5').wysihtml5({
        "stylesheets": [systemConfig.getBaseUrl()+"assets/global/plugins/bootstrap-wysihtml5/wysiwyg-color.css"]
    });
}
initWysihtml5();
</script>
 
	