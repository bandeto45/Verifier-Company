
import { createStore } from 'framework7';
import api from './request.api';

const store = createStore({
  state: {
    myinfo        : localStorage.myinfo ? api.decryptdata(localStorage.myinfo)  : [],
    liveness_res  : localStorage.liveness_res ? api.decryptdata(localStorage.liveness_res)  : [],
    iqa           : localStorage.iqa ? api.decryptdata(localStorage.iqa)  : [],
    iqaBlob       : false,
    liveness_serve  : localStorage.liveness_serve ? api.decryptdata(localStorage.liveness_serve)  : [],
    iqa_serve           : localStorage.iqa_serve ? api.decryptdata(localStorage.iqa_serve)  : [],


    face_compare  : localStorage.face_compare ? api.decryptdata(localStorage.face_compare)  : [],
    iqa_license   : [],
    lvs_license   : [],
    id_type_value : {
      'TIN' : 'TIN',
      'SSS' : 'SSS',
      'ID Card' : 'ID_CARD',
      'Drivers License' : 'DRIVING_LICENSE',
      'Passport'        : 'PASSPORT',
      'UMID'            : 'UMID'
    },
    country       : [
      {
        cn        : 'Philippines',
        cc        : 'PH',
        type      : [ 
          'Drivers License', 
          'ID Card',  
          'Passport', 
          'SSS', 
          'TIN', 
          'UMID' 
        ]
        
      },
      {
        cn        : 'Saudi Arabia',
        cc        : 'SA',
        type      : ['ID Card', 'Drivers License', 'Passport']
      },
      {
        cn        : 'Singapore',
        cc        : 'SG',
        type      : ['ID Card', 'Drivers License', 'Passport']
      }
    ]
  },
  getters: {
    id_type_value({state}){
      return state.id_type_value;
    },
    iqa({state}) {
      return state.iqa
    },
    liveness_res({ state }) {
      return state.liveness_res;
    },
    iqa_serve({state}) {
      return state.iqa_serve
    },
    liveness_serve({ state }) {
      return state.liveness_serve;
    },
    getdata({state}, v){
      return state[v];
    },
    country({state}){
      return state.country;
    },
    iqa_license({state}){
      return state.iqa_license;
    },
    lvs_license({state}){
      return state.lvs_license;
    },
    myinfo({state}){
      return state.myinfo;
    },
    face_compare({state}){
      return state.face_compare;
    },
    iqaBlob({state}){
      return state.iqaBlob;
    }
  },
  actions: {
    setdata({state}, v){
      state[v.key] = v.value;
      console.log("dispatch", state);
    }
  },
})
export default store;
