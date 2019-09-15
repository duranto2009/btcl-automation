<style>
	p.whiteSpace {
		white-space: pre;
	}
</style>

<div id="btcl-application-report" v-cloak="true">
	<btcl-body title="Report" subtitle="" :loader="loading">
		<btcl-portlet>
				<btcl-portlet>
					<div class="row">
						<div class="col-md-4" id="criteria">
							<h1 style="font-size: medium"><i class="fa fa-list">Criteria</i></h1>
							<div v-for="(element,index) in application.criteria">
								<input type="checkbox" :value="index" v-model ="selectedCriteria">
								<span>{{element.name}}</span>
							</div>
						</div>

						<div class="col-md-4" id="display">
							<h1 style="font-size: medium"><i class="fa fa-tv">Display</i></h1>
							<div v-for="(element,index) in application.display">
								<input type="checkbox" :value="index" v-model ="selectedDisplay">
								<span>{{element.name}}</span>
							</div>
						</div>
						<div class="col-md-4" id="orderby">
							<h1 style="font-size: medium"><i class="fa fa-reorder">Order By</i></h1>
							<div v-for="(element,index) in application.orderby">
								<input type="checkbox" :value="index" v-model ="selectedOrder">
								<span>{{element.name}}</span>
							</div>
						</div>
					</div>

				</btcl-portlet>

				<btcl-portlet v-if="selectedCriteriaList.length>0">

					<div v-for="element1 in selectedCriteriaList">
						<btcl-input v-if="element1.type == 'input'" :title="element1.name" :text.sync="element1.input">
						</btcl-input>
						<btcl-field v-if="element1.type == 'list'" :title="element1.name">
							<multiselect v-model="element1.input"
										 :options="element1.list"
										 placeholder="Select"
										 label="label">
							</multiselect>
						</btcl-field>

						<div  v-if="element1.type == 'date'">
							<btcl-field :title="'Activation From'">
								<btcl-datepicker :date.sync="element1.input1"></btcl-datepicker>
							</btcl-field>

							<btcl-field :title="'Activation To'">
								<btcl-datepicker :date.sync="element1.input2"></btcl-datepicker>
							</btcl-field>
						</div>

					</div>


				</btcl-portlet>
				<div class="custom-form-action">
					<div>
						<div class="text-center">
							<button type="reset" class="btn blue-hoki">Reset</button>
							<button type="submit" class="btn blue" @click="submitFormData">Submit</button>
						</div>
					</div>
				</div>
		</btcl-portlet>
		<div v-if="reportData.mapArrayList!=null && reportData.mapArrayList.length>0" class="portlet box portlet-btcl light">
			<div class="portlet-body">
				<div class="row" id="report-div">
					<div class="col-md-12">
						<div class="portlet light bordered">
							<div class="portlet-title">
								<div class="caption font-dark">
									<i class="icon-settings font-dark"></i> <span
										class="caption-subject bold uppercase">Report</span>
								</div>
								<div class="tools"></div>
							</div>
							<div class="portlet-body">
								<div>
									<button class="btn btn-primary" @click="csvExport(csvData)">Export to CSV </button>
									<button class="btn btn-default"  onclick="javascript:demoFromHTML();">Export to PDF</button>
									<button class="btn btn-primary" onclick="javascript:exportTableToExcel();">Export To Excel </button>
								</div>
								<div class="table" id ="reportDiv">
									<table class="table table-striped " id="reportTable"
										   style="width: 100%">
										<thead>
											<tr>
												<%--todo add table header in loop --%>
												<th v-for="element in Object.keys(reportData.mapArrayList[0])">{{element}}</th>
											</tr>
										</thead>
										<tbody>
										<%--todo add table content by looping in the vue elements--%>
											<tr v-for="element in reportData.mapArrayList">
												<td v-for="element1 in Object.values(element)"><p class="whiteSpace">{{element1}}</p></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</btcl-body>
</div>

<script src="${context}assets/report/jquery.dataTables.min.js"></script>
<script src="${context}assets/report/dataTables.buttons.min.js" ></script>
<script src="${context}assets/report/jszip.min.js"></script>
<%--
<script src="${context}assets/report/pdfmake.min.js" ></script>
--%>
<script src="${context}assets/report/vfs_fonts.js" ></script>
<script src="${context}assets/report/buttons.html5.min.js" ></script>
<script src="${context}assets/global/scripts/datatable.js"></script>
<script src="${context}assets/pages/scripts/table-datatables-scroller.min.js" ></script>
<script src="${context}assets/report/jspdf.debug.js"></script>
<script src="${context}report/connection/report.js"></script>

<script>
	function exportTableToExcel(){
		var downloadLink;
		var dataType = 'application/vnd.ms-excel';
		var tableSelect = document.getElementById('reportTable');
		var tableHTML = tableSelect.outerHTML.replace(/ /g, '%20');
		var filename = 'excel_data.xls';
		downloadLink = document.createElement("a");
		document.body.appendChild(downloadLink);
		if(navigator.msSaveOrOpenBlob){
			var blob = new Blob(['\ufeff', tableHTML], {
				type: dataType
			});
			navigator.msSaveOrOpenBlob( blob, filename);
		}else{
			downloadLink.href = 'data:' + dataType + ', ' + tableHTML;
			downloadLink.download = filename;
			downloadLink.click();
		}
	}
	function demoFromHTML() {
		var pdf = new jsPDF('p', 'pt', 'letter');
		source = $('#reportDiv')[0];
		specialElementHandlers = {
			'#bypassme': function (element, renderer) {
				return true
			}
		};
		margins = {
			top: 80,
			bottom: 60,
			left: 10,
			width: 700
		};
		pdf.fromHTML(
				source, // HTML string or DOM elem ref.
				margins.left, // x coord
				margins.top, { // y coord
					'width': margins.width, // max width of content on PDF
					'elementHandlers': specialElementHandlers
				},
				function (dispose) {
					pdf.save('export.pdf');
				}, margins);
	}
</script>