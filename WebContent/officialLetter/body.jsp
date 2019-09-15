<div class="row">
    <div class="col-md-12">

        <!-- Begin: Demo Datatable 1 -->
        <div class="portlet light portlet-fit portlet-datatable bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-settings font-dark"></i>
                    <span class="caption-subject font-dark sbold uppercase">Official Letter</span>
                </div>
            </div>
            <div class="portlet-body">
                <div class="table-container" style="">
                    <div id="datatable_ajax_wrapper" class="dataTables_wrapper dataTables_extended_wrapper no-footer">
                        <div class="row">
                            <div class="col-md-8 col-sm-12">
                                <div class="dataTables_paginate paging_bootstrap_extended" id="datatable_ajax_paginate">
                                    <div class="pagination-panel"> Page
                                        <a href="#" class="btn btn-sm default prev disabled">
                                            <i class="fa fa-angle-left"></i>
                                        </a>
                                        <input type="text" class="pagination-panel-input form-control input-sm input-inline input-mini"
                                               maxlenght="5" style="text-align:center; margin: 0 5px;">
                                        <a href="#" class="btn btn-sm default next">
                                            <i class="fa fa-angle-right"></i>
                                        </a> of <span class="pagination-panel-total">18</span>
                                    </div>
                                </div>
                                <div class="dataTables_length" id="datatable_ajax_length">
                                    <label>
                                        <span class="seperator">|</span>
                                        View
                                        <select name="datatable_ajax_length" aria-controls="datatable_ajax"
                                                class="form-control input-xs input-sm input-inline">
                                            <option value="10">10</option>
                                            <option value="20">20</option>
                                            <option value="50">50</option>
                                            <option value="100">100</option>
                                            <option value="150">150</option>
                                            <option value="-1">All</option>
                                        </select> records
                                    </label>
                                </div>
                                <div class="dataTables_info" id="datatable_ajax_info" role="status" aria-live="polite">
                                    <span class="seperator">|</span>
                                    Found total 178 records
                                </div>
                            </div>
                            <div class="col-md-4 col-sm-12">
                                <div class="table-group-actions pull-right">
                                    <span></span>

                                    <button class="btn btn-sm green table-group-action-submit">
                                        <i class="fa fa-check"></i> Submit
                                    </button>
                            </div>
                        </div>
                        </div>
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover table-checkable dataTable no-footer"
                                   id="datatable_ajax"
                                   aria-describedby="datatable_ajax_info" role="grid">
                                <thead>
                                    <tr>
                                        <th width="5%" class="sorting_disabled" rowspan="1" colspan="1"> Record&nbsp;# </th>
                                        <th width="15%" class="sorting_disabled" rowspan="1" colspan="1"> Date </th>
                                        <th width="200" class="sorting_disabled" rowspan="1" colspan="1"> Customer </th>
                                        <th width="10%" class="sorting_disabled" rowspan="1" colspan="1"> Ship&nbsp;To </th>
                                        <th width="10%" class="sorting_disabled" rowspan="1" colspan="1"> Price </th>
                                        <th width="10%" class="sorting_disabled" rowspan="1" colspan="1"> Amount </th>
                                        <th width="10%" class="sorting_disabled" rowspan="1" colspan="1"> Status </th>
                                        <th width="10%" class="sorting_disabled" rowspan="1" colspan="1"> Actions </th>
                                    </tr>
                        <tr role="row" class="filter"><td rowspan="1" colspan="1"> </td><td rowspan="1" colspan="1">
                            <input type="text" class="form-control form-filter input-sm" name="order_id"> </td><td rowspan="1" colspan="1">
                            <div class="input-group date date-picker margin-bottom-5" data-date-format="dd/mm/yyyy">
                                <input type="text" class="form-control form-filter input-sm" readonly="" name="order_date_from" placeholder="From">
                                <span class="input-group-btn">
                                                                    <button class="btn btn-sm default" type="button">
                                                                        <i class="fa fa-calendar"></i>
                                                                    </button>
                                                                </span>
                            </div>
                            <div class="input-group date date-picker" data-date-format="dd/mm/yyyy">
                                <input type="text" class="form-control form-filter input-sm" readonly="" name="order_date_to" placeholder="To">
                                <span class="input-group-btn">
                                                                    <button class="btn btn-sm default" type="button">
                                                                        <i class="fa fa-calendar"></i>
                                                                    </button>
                                                                </span>
                            </div>
                        </td><td rowspan="1" colspan="1">
                            <input type="text" class="form-control form-filter input-sm" name="order_customer_name"> </td><td rowspan="1" colspan="1">
                            <input type="text" class="form-control form-filter input-sm" name="order_ship_to"> </td><td rowspan="1" colspan="1">
                            <div class="margin-bottom-5">
                                <input type="text" class="form-control form-filter input-sm" name="order_price_from" placeholder="From"> </div>
                            <input type="text" class="form-control form-filter input-sm" name="order_price_to" placeholder="To"> </td><td rowspan="1" colspan="1">
                            <div class="margin-bottom-5">
                                <input type="text" class="form-control form-filter input-sm margin-bottom-5 clearfix" name="order_quantity_from" placeholder="From"> </div>
                            <input type="text" class="form-control form-filter input-sm" name="order_quantity_to" placeholder="To"> </td><td rowspan="1" colspan="1">
                            <select name="order_status" class="form-control form-filter input-sm">
                                <option value="">Select...</option>
                                <option value="pending">Pending</option>
                                <option value="closed">Closed</option>
                                <option value="hold">On Hold</option>
                                <option value="fraud">Fraud</option>
                            </select>
                        </td><td rowspan="1" colspan="1">
                            <div class="margin-bottom-5">
                                <button class="btn btn-sm green btn-outline filter-submit margin-bottom">
                                    <i class="fa fa-search"></i> Search</button>
                            </div>
                            <button class="btn btn-sm red btn-outline filter-cancel">
                                <i class="fa fa-times"></i> Reset</button>
                        </td></tr>
                        </thead>
                        <tbody> <tr role="row" class="odd"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="1"><span></span></label></td><td>1</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>1</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="even"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="2"><span></span></label></td><td>2</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>10</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="odd"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="3"><span></span></label></td><td>3</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>9</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="even"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="4"><span></span></label></td><td>4</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>1</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="odd"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="5"><span></span></label></td><td>5</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>4</td><td><span class="label label-sm label-info">Closed</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="even"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="6"><span></span></label></td><td>6</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>2</td><td><span class="label label-sm label-success">Pending</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="odd"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="7"><span></span></label></td><td>7</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>1</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="even"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="8"><span></span></label></td><td>8</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>7</td><td><span class="label label-sm label-info">Closed</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="odd"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="9"><span></span></label></td><td>9</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>7</td><td><span class="label label-sm label-info">Closed</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr><tr role="row" class="even"><td><label class="mt-checkbox mt-checkbox-single mt-checkbox-outline"><input name="id[]" type="checkbox" class="checkboxes" value="10"><span></span></label></td><td>10</td><td>12/09/2013</td><td>Jhon Doe</td><td>Jhon Doe</td><td>450.60$</td><td>10</td><td><span class="label label-sm label-danger">On Hold</span></td><td><a href="javascript:;" class="btn btn-sm btn-outline grey-salsa"><i class="fa fa-search"></i> View</a></td></tr></tbody>
                    </table></div><div class="row"><div class="col-md-8 col-sm-12"><div class="dataTables_paginate paging_bootstrap_extended"><div class="pagination-panel"> Page <a href="#" class="btn btn-sm default prev disabled"><i class="fa fa-angle-left"></i></a><input type="text" class="pagination-panel-input form-control input-sm input-inline input-mini" maxlenght="5" style="text-align:center; margin: 0 5px;"><a href="#" class="btn btn-sm default next"><i class="fa fa-angle-right"></i></a> of <span class="pagination-panel-total">18</span></div></div><div class="dataTables_length"><label><span class="seperator">|</span>View <select name="datatable_ajax_length" aria-controls="datatable_ajax" class="form-control input-xs input-sm input-inline"><option value="10">10</option><option value="20">20</option><option value="50">50</option><option value="100">100</option><option value="150">150</option><option value="-1">All</option></select> records</label></div><div class="dataTables_info"><span class="seperator">|</span>Found total 178 records</div></div><div class="col-md-4 col-sm-12"></div></div></div>
                </div>
            </div>
        </div>
        <!-- End: Demo Datatable 1 -->

    </div>
</div>