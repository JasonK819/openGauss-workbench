"use strict";(self["webpackChunkweb_ui"]=self["webpackChunkweb_ui"]||[]).push([[8957],{43610:function(e,t,a){a.d(t,{m:function(){return s}});a(57658);var n=a(95684);const s=(0,n.Q_)("modeling-common",{state:()=>({currentSelectData:{},selectNode:null,showConfig:!1,isRegisterNodes:[],rules:{},nodeEventData:null,i18n:null}),getters:{getI18n:e=>e.i18n,getCurrentSelectData:e=>e.currentSelectData,getSelectNode:e=>e.selectNode,getRules:e=>e.rules},actions:{setI18n(e){this.$patch((t=>{t.i18n=e}))},nodeEvent(e){this.nodeEventData=e},setIsResigterNodes(e){this.$patch((t=>{t.isRegisterNodes.push(e)}))},setSelectNode(e,t){this.$patch((a=>{a.showConfig=t,a.selectNode=e}))},setSelectData(){const e=[{id:5,name:"hs.article",fields:[{id:1,name:"article.type"},{id:2,name:"article.title"},{id:3,name:"article.content"}]},{id:1,name:"hs.user_test",fields:[{id:1,name:"user.name"},{id:2,name:"user.sex"},{id:3,name:"user.age"}]}];this.currentSelectData=e},setRule(e,t){this.$patch((a=>{a.rules[e]=t}))}}})},68957:function(e,t,a){a.r(t),a.d(t,{checkData:function(){return i},jsonFormat:function(){return r}});var n=a(43610),s=a(35589),l=a(90989);const c=(0,n.m)(),i=e=>{let t=!0,a="";return e&&e.cells&&Array.isArray(e.cells)&&e.cells.length>0&&e.cells.forEach((e=>{if("edge"!==e.shape){const n=c.getRules[e.data.cells_type];e.data&&(e.data.rule&&"function"===typeof e.data.rule?e.data.rule(e.data)||(t=!1,a+=`[${e.data.text}]`+l.Z.global.t("modeling.utils.index.5m78yfizcxc0")):n&&"function"===typeof n&&(n(e.data)||(t=!1,a+=`[${e.data.text}]`+l.Z.global.t("modeling.utils.index.5m78yfizcxc0"))))}})),!t&&a&&s.Z.error({position:"bottomRight",content:a,closable:!0,duration:1e4}),t},r=e=>{if(e){const t=e.toJSON();return t.cells.forEach((e=>{e.data&&e.data.cells_type?e.cells_type=e.data.cells_type:"edge"===e.shape&&(e.cells_type="line")})),t}return""}}}]);
//# sourceMappingURL=8957.53c54571.js.map