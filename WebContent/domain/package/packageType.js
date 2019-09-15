$(document)
		.ready(
				function() {
					var nNew = false;
					function editRow(nRow) {
						console.log(nRow);
						nRow.find(':disabled').closest('.disabled').removeClass('disabled');
						nRow.find(':disabled').removeAttr('disabled');
						nRow.find('.edit').addClass('save');
						nRow.find('.edit').removeClass('edit');
						nRow.find('.save').html('Save');
						nRow.find('.delete').addClass('cancel');
						nRow.find('.delete').removeClass('delete');
						nRow.find('.cancel').html('Cancel');
					}
					;
					function cancelEditRow(nRow) {
						console.log(nRow);
						nRow.find('input').attr("disabled", true);
						nRow.find('select').attr("disabled", true);
						nRow.find('.checker').addClass('disabled');
						nRow.find('.save').addClass('edit');
						nRow.find('.save').removeClass('save');
						nRow.find('.edit').html('Edit');
						nRow.find('.cancel').addClass('delete');
						nRow.find('.cancel').removeClass('cancel');
						nRow.find('.delete').html('Delete');
					}
					;
					function saveRow(nRow) {
						console.log(nRow);
						nRow.find('input').attr("disabled", true);
						nRow.find('select').attr("disabled", true);
						nRow.find('.checker').addClass('disabled');
						nRow.find('.save').addClass('edit');
						nRow.find('.save').removeClass('save');
						nRow.find('.edit').html('Edit');
						nRow.find('.cancel').addClass('delete');
						nRow.find('.cancel').removeClass('cancel');
						nRow.find('.delete').html('Delete');
					}
					;
					var table = $('#sample_editable_1');
					table.on('click', '.edit', function(e) {
						e.preventDefault();
						/*
						 * Get the row as a parent of the link that was clicked
						 * on
						 */
						var nRow = $(this).closest('tr');
						editRow(nRow);
					});
					table.on('click', '.cancel', function(e) {
						e.preventDefault();
						if (nNew) {
							nNew = false;
							table.find('tr:last').remove();
						} else {
							var nRow = $(this).closest('tr');
							console.log(nRow);
							cancelEditRow(nRow);
						}
					});
					table.on('click', '.delete', function(e) {
						e.preventDefault();

						if (confirm("Are you sure to delete this row ?") == false) {
							return;
						}
						var nRow = $(this).closest('tr');

						/* Write logic :Remove this row from front-end */
						

						/* Write logic: Remove this row from back-end */

						if (!nNew) {
							var url = "../../DomainAction.do?mode=removePackageType";
							var ID = nRow.find('input[name=ID]').val();
							var data = {
								ID : ID
							};
							$.ajax({
								type : "POST",
								url : url,
								data : data, // serializes the form's
								// elements.
								success : function(data) {
									nRow.remove();
									toastr.success("Package Type Deleted");
								},
								error: function(XMLHttpRequest, textStatus, errorThrown) {
									toastr.error("Failed");
								 }
								
							});
						}
						nNew = false;
					});
					table.on('click', '.save', function(e) {
						e.preventDefault();
						
						var nRow = $(this).closest('tr');
						/* Write logic :Save this row from front-end */
						
						/* Write logic: Save this row from back-end */
						var packageName = nRow.find('input[name=packageName]').val();
						if(packageName.length == 0){
							alert("Please write a package type name");
							return;
						}
						if (confirm("Are you sure to save this change ?") == false) {
							return;
						}
						var tld = nRow.find('select[name=tld]').val();
						var ID = nRow.find('input[name=ID]').val();
						var documentRequired = -1;
						var url;
						var data;
						if (nNew) {
							url = "../../DomainAction.do?mode=addPackageType";
							if (nRow.find('input[name=documentRequired]').is(':checked')) {
								documentRequired = 1;
							} else {
								documentRequired = 0;
							}
							data = {
								ID : ID,
								packageName : packageName,
								tld : tld,
								documentRequired : documentRequired
							};
						} else {
							url = "../../DomainAction.do?mode=editPackageType";
							documentRequired = nRow.find('input[name=documentRequired]').val();
							data = {
								ID : ID,
								packageName : packageName,
								tld : tld,
								documentRequired : documentRequired
							};
						}
						$.ajax({
							type : "POST",
							url : url,
							data : data, // serializes the form's elements.
							success : function(data) {
								saveRow(nRow);
								toastr.success("Package Type Saved.<br>Please add a Package for this Type");
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								toastr.error("Failed");
							 }
						});
						nNew = false;
					});
					$('#sample_editable_1_new')
							.click(
									function(e) {
										e.preventDefault();
										var newRowContent = "<tr><td><input type=\"hidden\" name=\"ID\" value=\"-1\"><input type=\"text\" name=\"packageName\" class=\"form-control\"  autocomplete=\"off\"> </td>"
												+ "<td><select class=\"form-control\" name=\"tld\">"
												+ "<option value=\"1\" >.bd</option>"
												+ "<option value=\"2\" >.বাংলা</option></select>"
												+ "<td><input type=\"checkbox\" name=\"documentRequired\" value=\"\"></div>"
												+ "</td><td><a class=\"save\" href=\"javascript:;\"> Save </a></td>"
												+ "<td><a class=\"cancel\" href=\"\"> Cancel </a></td> </tr>"
										table.find('tbody').append(newRowContent);
										nNew = true;
									});
				});