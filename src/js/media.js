let get_media = [];
export default class media{
    constructor(f7){
        this.tag = "[media]";
        this.f7 = f7;
        get_media = this;
        /*
        this.params
            call
            end_path
            ratio_square : 0/1
            tp  : gal/capture
            crop : 0/1
            w    : int
            h    : int
            id parameter
            wr : 
            hr :
        */
    }
    capture_image(tp='gal', file_desc = 'file_uri'){
        
        this.f7.preloader.show();
        try {
            navigator.camera.cleanup(function () { console.log("Cleaning..."); }, function (e) { console.log("Error getting picture: " + e); });
        } catch (error) {
            console.log(error);
        }
        var get_destinationType = {
            file_uri : Camera.DestinationType.FILE_URI,
            data_uri : Camera.DestinationType.DATA_URL,

        }
        var srcType = {gal : Camera.PictureSourceType.PHOTOLIBRARY, capture : Camera.PictureSourceType.CAMERA}
        var options = {
            // Some common settings are 20, 50, and 100
            quality: 100,
            destinationType: get_destinationType[file_desc],
            // In this app, dynamically set the picture source, Camera or photo gallery
            sourceType: srcType[tp],
            encodingType: Camera.EncodingType.PNG,
            mediaType: Camera.MediaType.PICTURE,
            correctOrientation: true,  //Corrects Android orientation quirks
        }
        let cam = this;
        console.log(options)
        let success_img = (img)=>{
            sessionStorage.setItem('uri', img);
            console.log(img)
            //lert(JSON.stringify(this.params));
            if(this.params.crop){
                let options_crop = {
                            quality: 100,
                            targetWidth : this.params.w,
                            targetHeight :this.params.h,
                            widthRatio:  this.params.w,//this.params.w == this.params.h  ? 1 : this.params.wr ? this.params.wr :  2,
                            heightRatio: this.params.h//this.params.w == this.params.h  ? 1 : this.params.hr ? this.params.hr : 1         
                            // targetWidth: this.params.w,
                            // targetHeight:this.params.h
                };
                var success_crop = (new_img)=> {
                    console.log(new_img)
                    cam[cam.params.call](new_img);
                    this.f7.preloader.hide();
                };
                var fail_crop = (e)=>{
                    console.log(e)
                    this.f7.preloader.hide();
                }
                if(tp!='gal'){
                    setTimeout(()=>{

                    plugins.crop(success_crop, fail_crop, img, options_crop);
                        // plugins.crop.promise(img, options_crop).then(function success (new_img) {
                        //     console.log(new_img)
                        //     cam[cam.params.call](new_img);
                        //     this.f7.preloader.hide();
                        // }).catch(function fail (e) {
                        //     console.log(e)
                        //     this.f7.preloader.hide();
        
                        // })
                    }, 1000)
                }else{
                    plugins.crop(success_crop, fail_crop, img, options_crop);
                    // plugins.crop.promise(img, options_crop).then(function success (new_img) {
                    //     console.log(new_img)
                    //     cam[cam.params.call](new_img);
                    //     this.f7.preloader.hide();
                    // }).catch(function fail (e) {
                    //     console.log(e)
                    //     this.f7.preloader.hide();
    
                    // })
                }
                
            }else{
            cam[cam.params.call](img);
            this.f7.preloader.hide();
            }
            
            
        }
        let error_img = (e)=>{
            get_media.f7.preloader.hide();
            this.f7.preloader.hide();

            console.log("Error getting picture: " + e);
        }
        navigator.camera.getPicture(success_img, error_img, options);   
    }


}