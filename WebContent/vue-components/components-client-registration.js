export let clientRegistrationFormGroup = {
    name : 'client-registration-form-group',
    template :
    `
        <div class="row">
					<div class="form-group">
						<label class="control-label col-sm-3 col-xs-12">
							{{title}}
						</label>
						<div class="col-sm-6 col-xs-12 ">
							<slot></slot>
						</div>
					</div>
				</div>  
    `,
    props : {
        title : String,
    },
};