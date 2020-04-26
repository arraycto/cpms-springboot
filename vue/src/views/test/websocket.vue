<template>
  <div class="app-container">
        <el-row>
            <el-col :span="8">
                <p>websocket测试</p>
                <div>
                     <el-input
                    type="textarea"
                    :autosize="{ minRows:5, maxRows: 10}"
                    placeholder="请输入内容"
                    v-model="message_text">
                    </el-input>
                </div>
                <div style="margin-top:20px;"><el-button type="primary" @click="wsSend">发布</el-button></div>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="8">
                <p>消息提示</p>
                <div id="msg-box">
                    <p v-for="(item,index) of msgList">
                        {{item.msg}}
                    </p>
                </div>
            </el-col>
        </el-row>
       
  </div>
</template>

<script>

import { importExcel} from '@/api/test'

export default {

    data() {
        let randomUser = "user:"+Math.random(); // 生成随机字符串作为用户名
        return {
            message_text:'',
            path:"ws://127.0.0.1:8080/websocket/"+randomUser,
            webSocket:null,
            msgList:[]
        }
    },

    methods: {

        initSocket(){

            if(typeof(WebSocket) === "undefined"){

                alert("您的浏览器不支持socket")

            }else{
                // 实例化socket
                this.webSocket = new WebSocket(this.path)
                // 监听socket连接
                this.webSocket.onopen = this.wsOpen
                // 监听socket错误信息
                this.webSocket.onerror = this.wsError
                // 监听socket消息
                this.webSocket.onmessage = this.wsMessage
            }
        },

        wsOpen(event){
            console.log("已连接")
        },

        wsError(event){
            console.log("连接错误")
        },

        wsMessage(event){
            console.log("监听wsServer消息")  
            this.msgList.push(JSON.parse(event.data))  
        },
        wsSend(){
            let msg = this.message_text
          
            this.webSocket.send(msg)
        }
    },

    created() {
        this.initSocket()
    },

   
}
</script>

<style lang="scss" scoped>

</style>
