<div id=app>
<btcl-portlet>
	<div class="table-responsive" v-show="files.length > 0">
		<table class="table">
	        <thead>
	            <tr>
	                <th>#</th>
	                <th>Name</th>
					<th>Download Link</th>
	                <th>Size</th>
	                <th>Progress</th>
	                <th>Status</th>
	                <th>Remove</th>
	            </tr>
	        </thead>
	        <tbody>
	            <tr v-for="(file, fileIndex) in files">
	                <td>{{fileIndex+1}}</td>
	                <td>{{file.name}}</td>
					<td>link</td>
	                <td>{{isNaN(file.size) ? "--" : (Number.isInteger(file.size/(1024*1024)) ? file.size/(1024*1024) : (file.size/(1024*1024)).toFixed(3) ) + ' Mb'}}</td>
	                <td>{{parseInt(file.progress) + "%"}}</td>
	                <td>{{file.success ? "Complete" : "Pending"}}</td>
	                <td><button type=button class="btn btn-outline" @click.prevent="remove(file)">Remove</button></td>
	            </tr>
	        </tbody>
	    </table>
	</div>
	<div class=row>
		<div class="col-md-3 col-md-offset-6">
			<vue-upload-component ref="upload" v-model="files" post-action="/file" :multiple=true
								  :data=
										  "{
										    'moduleId': 7,
								  			'componentId': 2,
								  			'applicationId': 12536,
								  			'stateId': 125
								  			}"
				accept="image/png,image/gif,image/jpg,image/jpeg,application/pdf" extensions="png,gif,jpg,jpeg,pdf"
				:thread="5"
				@input-filter="inputFilter"
				class="btn btn-block btn-default"
			>Select Files</vue-upload-component>
		</div>
		<div class=col-md-3>
			<button type=button class="btn btn-block green-haze" v-show="!$refs.upload || !$refs.upload.active" @click.prevent="$refs.upload.active = true" type="button">Start upload</button>		
		</div>
	</div>
</btcl-portlet>
  
</div>

<script>
var vue = new Vue({
	el: '#app',
	data: function () {
	    return {
			files: [],
			fileNameArray: []
		}
	},
	watch: {
		files: {
			deep: true,
			handler: function(val){
				this.fileNameArray = [];
				this.files.forEach(function(currentValue,index,array){
					this.fileNameArray.push(currentValue.name);
				},this);
			}
		}
	},
	methods: {
		remove: function(file){
			this.$refs.upload.remove(file);
		},
		inputFilter(newFile, oldFile, prevent) {
			if (newFile && !oldFile) {
				if (/(\/|^)(Thumbs\.db|desktop\.ini|\..+)$/.test(newFile.name)) {
					return prevent();
				}
				if (/\.(php5?|html?|jsx?)$/i.test(newFile.name)) {
					return prevent();
				}
				if (!(/\.(png?|gif?|jpg?|jpeg?|pdf?)$/i.test(newFile.name))){
					toastr.error(newFile.name + " has invalid file extension");
					return prevent();
				}
			}
			if (newFile && oldFile) {
				//return prevent();
			}
			if (!newFile && oldFile) {
				alert("removed");
				console.log('remove', oldFile)
			}
		}
	}
});
</script>