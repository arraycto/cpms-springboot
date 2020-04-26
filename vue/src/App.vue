<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
import { resetRouter , authorRoutes,constantRoutes } from '@/router'
export default {
  name: 'App',

  /*解决刷新页面vuex数据丢失问题*/
  // created () {

  //   //在页面刷新时将vuex里的信息保存到localStorage里
  // 	window.addEventListener("beforeunload",()=>{
  // 	  localStorage.setItem("store-data",JSON.stringify(this.$store.state))
  // 	})
  
  // 	//在页面加载时读取localStorage里的状态信息
  //   localStorage.getItem("store-data") && this.$store.replaceState(Object.assign(this.$store.state,JSON.parse(localStorage.getItem("store-data"))));
    
   
  //     // this.$router.options.routes = constantRoutes.concat(authorRoutes);
  //     // this.$router.addRoutes(constantRoutes.concat(authorRoutes))
  // }
}
</script>
