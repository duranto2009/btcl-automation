$("#crmComplainAddForm").on( "submit", function( event ){
    event.preventDefault();

    var url = $(this).attr( 'action' );
    var param = {};

    param['currentDescription'] = $("#currentDescription").val();

    console.log( url );
    console.log( param );

    callAjax( url,param,complainAddCallback,"POST" );
});

function complainAddCallback( data ){

    console.log( data );

    if( data['responseCode'] == 1 ){

        toastr.success(data['msg']);
    }
    else{

        toastr.error( data['msg'] );
    }
}

