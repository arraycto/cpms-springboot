<template>
  <div class="app-container">
        <el-row>
            <el-col :span="6">
                <p>多文件上传（可拖动上传）<p/>
                <el-upload  ref="attachFileWrap" class="attachFileWrap" :show-file-list="false" :auto-upload="false" drag multiple  action :on-change="multipleFileUpload"></el-upload>
            </el-col>
            <el-col :span="6">
                <p>分片上传(大于300k)<p/>
                <el-upload  ref="chunkFileWrap" class="attachFileWrap" :show-file-list="false" :auto-upload="false" drag  action :on-change="chunkFileUpload"></el-upload>
            </el-col>
            <el-col :span="4">
                <el-button class="el-icon-download" type="primary" round @click="userExcel">导出excel</el-button>
            </el-col>

            <el-col :span="6">
                <p>导入excel<p/>
                <el-upload  ref="excelFileWrap" class="attachFileWrap" :show-file-list="false" :auto-upload="false" drag  action :on-change="excelUpload"></el-upload>
            </el-col>
        </el-row>
  </div>
</template>

<script>

import { upload ,chunkUpload , mergeChunk,exportExcel,importExcel} from '@/api/test'

export default {

    data() {
        return {
          
        }
    },

    created() {},

    methods: {
        multipleFileUpload($file){
            let formData = new FormData();
                formData.set("file", $file.raw);
                
            upload(formData).then(res=>{

                console.log(res)
            })
        },

        chunkFileUpload($file){
            let file = $file.raw,
                chunkSize= 2*1024,//单位是：bite
                chunksList=[], //保存分片数据
                token = (+ new Date()),//时间戳
                name =file.name,
                chunkCount=0,
                sendChunkCount=0,
                cur = 0;
             
              
            //拆分文件 像操作字符串一样
            if(file.size > chunkSize){

                while (cur < file.size) {
                    chunksList.push( file.slice(cur, cur + chunkSize) );
                    cur += chunkSize;
                }
               
            }else{

                chunksList.push(file);
            }

          
            chunkCount=chunksList.length;//分片的个数 


            for(var i=0;i< chunkCount;i++){
                var fd = new FormData();   //构造FormData对象
                fd.append('token', token);
                fd.append('file', chunksList[i]);
                fd.append('filename', name);
                fd.append('index', i);
                
                // 需要做控制并发的http请求   可以参考 https://juejin.im/post/5e367f6951882520ea398ef6
                // 实现断点续传 可以参考 https://juejin.im/post/5dff8a26e51d4558105420ed#heading-16
                chunkUpload(fd).then(res=>{
                    if(res.code==200) {
                        sendChunkCount++
                        if(sendChunkCount===chunkCount){//上传完成，发送合并请求
                            console.log('上传完成，发送合并请求');
                            var mergeForm = new FormData();
                            mergeForm.append('token',token);
                            mergeForm.append('chunkCount',chunkCount);
                            mergeForm.append('filename',name);
                            mergeChunk(mergeForm).then(res=>{
                                if(res==200){
                                    console.log("合并上传分片成功")
                                }
                            })
                        }
                    }
                })
            }
        },

        userExcel(){
            exportExcel().then(res=>{
                let content = res
                let aTag = document.createElement('a');
                let blob = new Blob([content]);
                aTag.download = 'ExcelTest.xls';
                aTag.href = URL.createObjectURL(blob); // 创建二进制URL对象
                aTag.click();
                URL.revokeObjectURL(blob); // 释放对象URL资源
            })
        },

        excelUpload($file){
            let formData = new FormData();
                formData.set("file", $file.raw);
                
            importExcel(formData).then(res=>{

                console.log(res)
            })
        },
    }
}
</script>

<style lang="scss" scoped>

</style>
