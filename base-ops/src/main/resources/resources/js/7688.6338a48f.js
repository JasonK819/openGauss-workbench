"use strict";(self["webpackChunkweb_ui"]=self["webpackChunkweb_ui"]||[]).push([[7688],{83744:function(e,t){t.Z=(e,t)=>{const o=e.__vccOpts||e;for(const[n,l]of t)o[n]=l;return o}},77688:function(e,t,o){o.r(t),o.d(t,{default:function(){return p}});var n=o(70821),l=o(96336),s=o(882),r=o(72453),c=o(57330);const a={class:"d-content"},i={class:"select-role"};var d=(0,n.defineComponent)({emits:["success"],setup(e,{expose:t,emit:o}){const{t:d}=(0,c.QT)(),u=l.ZP.Option,m=[],p=(0,n.reactive)({show:!1,title:d("modeling.components.CUUser.5mpu44y3ivw0"),loading:!1,userList:[],roles:[]}),f=()=>{p.show=!1},k=(e,t)=>{p.show=!0,p.roles=e,p.userList=m.filter((e=>-1===t.findIndex((t=>t.name===e.name))))},w=()=>{o("success",JSON.parse(JSON.stringify(p.userList.filter((e=>e.selected))))),f()};return t({open:k}),(e,t)=>((0,n.openBlock)(),(0,n.createBlock)((0,n.unref)(s.Z),{class:"cu-user-container",visible:(0,n.unref)(p).show,"onUpdate:visible":t[0]||(t[0]=e=>(0,n.unref)(p).show=e),title:(0,n.unref)(p).title,"ok-text":e.$t("modeling.components.CUUser.5mpu44y3ic40"),"confirm-loading":(0,n.unref)(p).loading,"cancel-text":e.$t("modeling.components.CUUser.5mpu44y3iso0"),onOk:w,onCancel:f,"modal-style":{width:"400px"}},{default:(0,n.withCtx)((()=>[(0,n.createElementVNode)("div",a,[((0,n.openBlock)(!0),(0,n.createElementBlock)(n.Fragment,null,(0,n.renderList)((0,n.unref)(p).userList,((e,t)=>((0,n.openBlock)(),(0,n.createElementBlock)("div",{class:"d-row",key:`userlist${t}`},[(0,n.createVNode)((0,n.unref)(r.Z),{modelValue:e.selected,"onUpdate:modelValue":t=>e.selected=t},{default:(0,n.withCtx)((()=>[(0,n.createTextVNode)((0,n.toDisplayString)(e.name),1)])),_:2},1032,["modelValue","onUpdate:modelValue"]),(0,n.withDirectives)((0,n.createElementVNode)("div",i,[(0,n.createVNode)((0,n.unref)(l.ZP),{modelValue:e.role,"onUpdate:modelValue":t=>e.role=t},{default:(0,n.withCtx)((()=>[((0,n.openBlock)(!0),(0,n.createElementBlock)(n.Fragment,null,(0,n.renderList)((0,n.unref)(p).roles,((e,t)=>((0,n.openBlock)(),(0,n.createBlock)((0,n.unref)(u),{key:`role${t}`,value:e.id},{default:(0,n.withCtx)((()=>[(0,n.createTextVNode)((0,n.toDisplayString)(e.name),1)])),_:2},1032,["value"])))),128))])),_:2},1032,["modelValue","onUpdate:modelValue"])],512),[[n.vShow,e.selected]])])))),128))])])),_:1},8,["visible","title","ok-text","confirm-loading","cancel-text"]))}}),u=o(83744);const m=(0,u.Z)(d,[["__scopeId","data-v-0add7196"]]);var p=m}}]);
//# sourceMappingURL=7688.6338a48f.js.map