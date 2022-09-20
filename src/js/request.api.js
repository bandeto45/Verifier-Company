
import CryptoJS from "crypto-js"
import app from 'framework7';
var global_uri = "https://sg-api.advance.ai/intl/openapi"; 

var ajx_request = (params)=>{
    console.log(params, app.request)
    var success_response = [];
    app.request({
        url : global_uri+params.path,
        data : params.dataset,
        crossDomain: true,        
        dataType: 'json',        
        method: params.get ? 'GET' : 'POST',             
        cache: true,  
        // headers : { "APP-KEY" : "91eb3aadc9a28b36"},

        success : (res)=>{
            success_response = res;
            console.log(res);
        },
        complete : ()=>{

        },
        error : ajax_error
    })
    var ajax_error = (jqXHR)=>{
        console.log(app)
        if (jqXHR.status === 0) {        
            
            app.notification.create({
                icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
                title:  "No Internet Connection",
                subtitle: "Please try again!",
                text: 'Click (x) button to close',
                closeButton: true,
                closeTimeout: 4000,
            }).open();      
        } else if (jqXHR.status === 404) {        
            
            app.notification.create({
                icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
                title:  "Status 404",
                subtitle: "Requested page not found.",
                text: 'Click (x) button to close',
                closeButton: true,
                closeTimeout: 4000,
            }).open();       
        } else if (jqXHR.status === 403) {        
            
            app.notification.create({
                icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
                title:  "Forbidden [403]",
                subtitle: "You don't have a permission to access / on this server.",
                text: 'Click (x) button to close',
                closeButton: true,
                closeTimeout: 4000,
            }).open();        
        } else if (jqXHR.status === 500) {       
             
            app.notification.create({
                icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
                title:  "Error 500",
                subtitle: "Internal Server Error [500]",
                text: 'Click (x) button to close',
                closeButton: true,
                closeTimeout: 4000,
            }).open();  
        } 
        // else if (jqXHR.status === 429) {       
        //     ajax.successajax();      
        // } 
        // else {        
        //     alert('Uncaught Error.\  ' + JSON.stringify(jqXHR));      
        // }  
    }
}

export default ajx_request;
