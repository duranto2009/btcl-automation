<script id="template-upload" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-upload fade">
		<!--
		<td>
            <span class="preview"></span>
        </td>
		-->
        <td class="name" width="30%">
			<p class="name">{%=file.name%}</p>
            <strong class="error text-danger"></strong>
		</td>
        <td class="size" width="10%">
			<span>{%=o.formatFileSize(file.size)%}</span>
		</td>
     
        <td  colspan="2" width="40%">
           <p class="size">{%=o.formatFileSize(file.size)%}</p>
             <div class="progress progress-striped active" role="progressbar" aria-valuemin="0" aria-valuemax="100" aria-valuenow="0">
               <div class="progress-bar progress-bar-success" style="width:0%;"></div>
             </div>
        </td>
       <td class="size" width="10%"><span title="size" >{%=file.document_type%}</span></td>
        <td  width="10%" align="right">{% if (!i) { %}
            <button class="btn btn-sm red cancel"> <i class="fa fa-ban"></i>  <span> cancel </span> </button>
    	    {% } %}
		</td>
    </tr>
{% } %}
</script>
<!-- The template to display files available for download -->
<script id="template-download" type="text/x-tmpl">
{% for (var i=0, file; file=o.files[i]; i++) { %}
    <tr class="template-download fade">
        {% if (file.error) { %}
            <td class="name" width="30%"><span>{%=file.name%}</span></td>
            <td class="size" width="40%"><span>{%=o.formatFileSize(file.size)%}</span></td>
            <td class="error" width="30%" colspan="2"><span class="label label-danger">  {%=file.error%}  </span></td>
        {% } else { %}
			<!--
			<td>
				{% if (file.thumbnail_url) { %}
					<span class="preview">
						<a href="{%=file.url%}" title="{%=file.name%}" download="{%=file.name%}" data-gallery><img src="{%=file.thumbnail_url%}"></a>
					</span>
                {% }else { %}
					No Preview
				{% } %}
       		 </td>
			-->
			<td class="size" width="20%"><span title="size" >{%=file.document_type%}</span></td>
            <td class="name" width="30%">
                <a href="{%=file.url%}" title="{%=file.name%}" {%=file.thumbnail_url?'data-gallery':''%} download="{%=file.name%}">{%=file.name%}</a>
            </td>

            <td class="size" width="20%"><span title="size" >{%=o.formatFileSize(file.size)%}</span></td>
            <td colspan="2"></td>
        {% } %}
         <td width="10%" align="right">
            <button data-id="{%=file.modified_name%}"  title="cancel" class="btn default btn-sm delete" data-type="{%=file.delete_type%}" data-url="{%=file.delete_url%}"{% if (file.delete_with_credentials) { %} data-xhr-fields='{"withCredentials":true}'{% } %}>
                <i class="fa fa-times"></i>
            </button>
        </td>
    </tr>
{% } %}
</script>

