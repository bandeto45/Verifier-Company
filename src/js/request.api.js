
import CryptoJS from "crypto-js"
import { request} from 'framework7/bundle';
var global_uri = "https://chocoekyc.com/ekyc/api/mobile_api.php"; 

var contentType = {
    multi_form : "multipart/form-data",
    multi_form_png : "multipart/form-data;image/png",
    text       : "text/plain",
    app_form   : "application/x-www-form-urlencoded"
}
/**
 *  params value
 *  contentType : multi_form, text, app_form
 *  get         : 1
 *  dataset     : {}
 *  local       : 
 *  exec        : function
 *  back    
 *  no_result   : 
 *  alert_error
 *  offprocessData
 *  headers
 */

var get_request = {
    uri  : ()=>{
        return global_uri;
    },
    encryptdata :(v)=>{
        return CryptoJS.AES.encrypt(JSON.stringify(v), 'Secret key pro').toString()

    },
    decryptdata : (v)=>{
        var decrypted  = CryptoJS.AES.decrypt(v, 'Secret key pro');
        var originalText = JSON.parse(decrypted.toString(CryptoJS.enc.Utf8));
        
        console.log("[DECRYPT]", originalText);
        
        return originalText
        // var decrypted = thisApi.CryptoJS.decrypt(v, 'Secret key pro')
        // console.log("[DECRYPT]", decrypted);
        // var originalText = decrypted.toString(thisApi.enc_utf8);
        // return originalText
    },
    checkLoader : (buttonloader='btn-loader')=>{
        let get_button = app.f7.$('.'+buttonloader); 
        let get_dataset = get_button.dataset();
        console.log(get_dataset);
        if(get_dataset.clicked){
            return true;
        }else{
            return false;
        }
    },
    showLoader : (buttonloader='btn-loader')=>{
        let get_button = app.f7.$('.'+buttonloader); 
        let get_dataset = get_button.dataset();
        if(!get_dataset.clicked){
            get_button.attr("data-clicked", 1);
            
            setTimeout(()=>{
                get_button.html("");
                app.f7.preloader.showIn('.'+buttonloader)
            }, 100);
            return true;
        }else{
            return false;
        }
    },
    hideLoader : (buttonloader='btn-loader', button_preserved_value='SUBMIT')=>{
        let get_button = app.f7.$('.'+buttonloader); 
        get_button.attr("data-clicked", 0);
        app.f7.preloader.hideIn('.'+buttonloader)
        setTimeout(()=>{
            get_button.html(button_preserved_value);
        }, 500)
    },
    dispatch : (store, data)=>{
        const setdata = {
            key : store,
            value : data
        }
        app.f7.store.dispatch("setdata", setdata);
        console.log("[app][store]", app.f7.store.getters[store]);
    },
    setLocal : (local, v)=>{
        localStorage.setItem(local, get_request.encryptdata(v));
    },
    notif : (message, title="Alert", icon="warning")=>{
        app.f7.notification.create({
            icon: '<i class="icon material-symbols">'+icon+'</i>',
            title:  title,
            subtitle: message,
            text: 'Click (x) button to close',
            closeButton: true,
            closeTimeout: 10000,
        }).open();       
    },
    check_network : ()=>{
        document.addEventListener("offline", onOffline, false);
        function onOffline() {
            get_request.notif("No Internet Connection", "Please try again!")
            // Handle the offline event
        }
        document.addEventListener("online", oonline, false);
        function oonline() {
            get_request.notif("Network is back!", "Success")
            // Handle the offline event
        }
    },
    myinfo  : (v, code='', buttonloader)=>{
        const myinfo = app.f7.store.getters.myinfo;
        console.log(app.f7)
        let func = v; 
        const params = {
            json : 1,
            dataset     : {
                code 			: code ? code : myinfo.value.code,
                validate_code 	: 1 
            },
            local 		: 'myinfo',
            exec        : func,
            alert_error : 1,
            store       : 'myinfo',
        }
        if(buttonloader){
            params.buttonloader = buttonloader.btn;
            params.buttonval = buttonloader.val;
        }
        console.log(func);
        get_request.request(params)
    },
    
    get_form : (id)=>{
        return app.f7.form.convertToData(id);
    },
    request : (v)=>{
        let params = v;
        var option = {
            url: global_uri,
            method : 'POST',
            cache: params.no_cache ? false : true,   
            crossDomain: true,     

            error  : error
        };
        if(params.contentType){
            option.contentType = contentType[params.contentType]
        }
        if(params.json){
            option.dataType = 'json'
        }
        
        if(params.sync){
            option.async = false;
        }
        if(params.get){
            option.method = 'GET'
        }
        if(params.dataset){
            option.data = params.dataset
        }
        if(params.headers){
            option.headers = params.headers;
            
        }
        if(params.offprocessData){
            option.processData = false;
        }
        console.log('[option]', option)
        request(option).then((res) => {
            if(params.buttonloader){
                console.log(params.buttonloader);
                if(get_request.checkLoader(params.buttonloader)){
                    get_request.hideLoader(params.buttonloader, params.buttonval)
                }
            }
            let subfunction = ()=>{
                
                
                
                if(params.local){
                    get_request.setLocal(params.local, res.data)
                }
                if(params.store){
                    console.log("[app]", app)
                    
                    get_request.dispatch(params.store, res.data)

                    
                }
                if(params.exec){
                    console.log("Calling function")
                    params.exec(res.data);
                }
                if(params.back){
                    app.f7.views.main.router.back();
                }
            }
            console.log(res.data)
            if(res.data.result || params.no_result){
                console.log("no alert init") 

                if(params.alert){
                   console.log("alert init") 
                   app.f7.dialog.alert(res.data.message, res.data.msg_title, subfunction);
                }else{
                    console.log("no alert init") 
                    subfunction();
                }
            }else{
                console.log("error init") 
                
                if(params.alert_error){
                    console.log("alert_error init", app) 
                    app.f7.dialog.alert(res.data.message, res.data.msg_title, subfunction);
                }
            }
        });
    }
}

const error = (err)=>{
    console.log(err)
    if (err.status === 0) {        
        app.f7.notification.create({
            icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
            title:  "No Internet Connection",
            subtitle: "Please try again!",
            text: 'Click (x) button to close',
            closeButton: true,
            closeTimeout: 4000,
        }).open();      
    } else if (err.status === 404) {        
        app.f7.notification.create({
            icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
            title:  "Status 404",
            subtitle: "Requested page not found.",
            text: 'Click (x) button to close',
            closeButton: true,
            closeTimeout: 4000,
        }).open();       
    } else if (err.status === 403) {        
        app.f7.notification.create({
            icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
            title:  "Forbidden [403]",
            subtitle: "You don't have a permission to access / on this server.",
            text: 'Click (x) button to close',
            closeButton: true,
            closeTimeout: 4000,
        }).open();        
    } else if (err.status === 500) {       
        app.f7.notification.create({
            icon: '<i class="icon f7-icons text-color-red">exclamationmark_triangle_fill</i>',
            title:  "Error 500",
            subtitle: "Internal Server Error [500]",
            text: 'Click (x) button to close',
            closeButton: true,
            closeTimeout: 4000,
        }).open();  
    } 
}
export default get_request;