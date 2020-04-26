<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole" v-if="checkPermission('/role/add/**')" size="mini"><i class="el-icon-circle-plus-outline"></i>&nbsp;添加角色</el-button>
    <div class="user-table">
        <el-table
          :data="allRole"
          border
          style="width: 100%">
             <el-table-column
              prop="sortnum"
              align="center"
              label="序号"
              type="index"
              width="180">
            </el-table-column>
            <el-table-column
              prop="roleName"
              align="center"
              label="姓名"
              width="180">
            </el-table-column>

            <el-table-column
              prop="roleDescript"
              align="center"
              label="描述">
            </el-table-column>
            <el-table-column
              prop="handle"
              align="center"
              label="操作">
              <template slot-scope="scope">
                  <el-button type="primary" size="small" @click="handleEditRole(scope.$index, allRole)" v-if="checkPermission('/role/edit/**')">编辑</el-button>
                  <el-button type="danger" size="small" @click="handleDelete(scope.$index, allRole)" v-if="checkPermission('/role/delete/**')">删除</el-button>
              </template>
            </el-table-column>
        </el-table>
        <div class="page-bar">
            <el-pagination
            @current-change="handleCurrentChange"
            :current-page.sync="currentPage"
            :page-size="pageSize"
            layout="total, prev, pager, next"
            :total="count"
            >
            </el-pagination>
        </div>
    </div>
    <el-dialog :visible.sync="dialogVisible" width="65%" :title="dialogType==='add'?'添加角色':'编辑角色'" >
      <el-form :model="roleInfo" label-width="80px" label-position="left">
        <el-form-item label="角色名">
          <el-input v-model="roleInfo.name" placeholder="角色名..."  style="width:30%;"/>
        </el-form-item>
        <el-form-item label="描述">
          <el-input
            v-model="roleInfo.des"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="角色描述..."
            style="width:30%;"
          />
        </el-form-item>
         <el-form-item label="角色权限">
        </el-form-item>
        <div class="auhor-container">
            <div class="permit-header">
                <li>显示菜单</li>
                <li>操作权限</li>
            </div>
            <div class="menu-permit-list">
                <ul v-for="(parent,index) in permitRoutes" class="menu-box">
                    <div v-if="!parent.hidden" class="menu-wrapper">
                        <li class="parent-menu">
                            <el-checkbox-group v-model="checkedMenus" @change="handleCheckedMenuChange">
                                <el-checkbox  :key="parent.menuUrl"   :label="parent.menuUrl" @click.native="parentIndex=index"  @change="checkedAllChildMenu">{{parent.meta.title}}</el-checkbox>
                            </el-checkbox-group>
                        </li>
                        
                        <li class="children-menu-permit" v-for="child in parent.children">
                            <ul>
                                <li class="children-menu">
                                    <el-checkbox-group v-model="checkedMenus" @change="handleCheckedMenuChange">
                                        <el-checkbox  :label="child.menuUrl" :key="child.menuUrl" @click.native="parentIndex=index" @change="checkedChildMenu">{{child.meta.title}}</el-checkbox>
                                    </el-checkbox-group>
                                </li>
                                <li class="permit-list">
                                    <el-checkbox-group v-model="checkedpermits" @change="handleCheckedpermitChange">
                                        <el-checkbox v-for="permit in child.permit" :label="permit.url" :key="permit.url">{{permit.name}}</el-checkbox>
                                    </el-checkbox-group>
                                </li>
                                <li class="clear-float"></li>
                            </ul>
                        </li>
                        <li class="permit-list parent-permit-list" v-if="parent.permit">
                            <el-checkbox-group v-model="checkedpermits" @change="handleCheckedpermitChange">
                                <el-checkbox v-for="permit in parent.permit" :label="permit.url" :key="permit.url">{{permit.name}}</el-checkbox>
                            </el-checkbox-group>
                        </li>
                    </div>
                </ul>
                <ul class="clear-float"></ul>
            </div>
           
        </div>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false" >取消</el-button>
        <el-button type="primary" @click="confirmRole" >确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import { permitRoutes } from '@/router'
import { Message } from 'element-ui'
import { getAllRole,addRole,deleteRole,editRole} from '@/api/role'
import { checkPermission } from '@/utils/index'
const defaultRole = {
  name: '',
  des:'',
}

export default {

  data() {
    return {
        roleInfo:Object.assign({},defaultRole),
        dialogType: 'add',
        dialogVisible: false,
        roles:[],
        permitRoutes:[], // 路由菜单和相关权限
       
        allRole: [],
        checkedMenus:[],  // 选中的菜单路由
        checkedpermits:[], // 选中的操作权限
        currentPage:1,
        pageSize:10,
        count:null,
        parentIndex:null,
    }
  },

  inject : ['reload'],

  created() {
        this.getRoleList()
        this.permitRoutes  = permitRoutes.filter((item,index,array)=>{
             return !item.hidden
        })
  },
  methods: {
    checkPermission, // 判断权限公共函数
    getRoleList(){
        getAllRole({page:this.currentPage,pageSize:this.pageSize}).then(res =>{
            if(res.code ==1000) {

                this.allRole = res.data.lists
                this.count   = res.data.totalCount

            }else{
                Message({
                    message: res.msg || 'warning',
                    type: 'warning',
                })
            }
        })
    },
    confirmRole() {
        if(this.roleInfo.name.trim().length <=0) {
            Message({
				message: '请填写角色名',
				type: 'warning',
            })

			return
        }

        if(this.checkedMenus.length <=0 ) {
            Message({
				message: '请选择角色菜单',
				type: 'warning',
            })

			return
        }

        if(this.checkedpermits.length <=0 ) {
            Message({
				message: '请选择角色操作权限',
				type: 'warning',
            })
			return
        }
      
        let roleInfo = {
            roleName:this.roleInfo.name,
            roleDescript:this.roleInfo.des,
            pagePermission:this.checkedMenus.join(","),
            handlePermission: this.checkedpermits.join(",")
        }
        
        if(this.dialogType== 'add') {
            addRole(roleInfo).then(res=>{

                if(res.code ==1000) {
                    this.roles = res.data
                    Message({
                        message: res.msg || 'success',
                        type: 'success',
                    })

                    this.reload()
                }else{

                    Message({
                        message: res.msg || 'warning',
                        type: 'warning',
                    })
                }
            })

        }else{
            roleInfo.roleId = this.roleInfo.roleId

            editRole(roleInfo).then(res=>{

                if(res.code ==1000) {
                    this.roles = res.data
                    Message({
                        message: res.msg || 'success',
                        type: 'success',
                    })

                this.reload()
                }else{
                    Message({
                        message: res.msg || 'warning',
                        type: 'warning',
                    })
                }
            })
        }      
    },

    handleCurrentChange(val) {
		this.currentPage = val
      	this.getRoleList()
    },
    handleAddRole() {
       this.roleInfo = Object.assign({},defaultRole)
       this.checkedMenus   = []  
       this.checkedpermits = [] 
       this.dialogVisible = true
       
       this.dialogType    = "add"
    },

    handleEditRole(index, rows) {
        this.roleInfo.name   = rows[index].roleName
        this.roleInfo.roleId = rows[index].roleId
        this.roleInfo.des    = rows[index].roleDescript

        this.checkedMenus   = rows[index].rolePermission.pagePermission.split(",")
        this.checkedpermits = rows[index].rolePermission.handlePermission.split(",")

        this.dialogVisible = true
        this.dialogType    = "edit"
    },

    // 选择菜单
    handleCheckedMenuChange(checked) {
        this.checkedMenus = checked
     
    },

    // 选择权限
    handleCheckedpermitChange(checked) {
        this.checkedpermits = checked   

    },

    handleDelete(index, rows){
        let delRoleName = rows[index].roleName


        this.$confirm(`确定要删除[${delRoleName}], 是否继续?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'

        }).then(() => {
            
            deleteRole({roleId:rows[index].roleId}).then(res =>{
                if(res.code ==1000) {
                    Message({
                        message: res.msg || 'success',
                        type: 'success',
                    })

                }else{
                    Message({
                        message: res.msg || 'success',
                        type: 'warning',
                    })
                }
            })
            
            this.reload()
        }).catch(() => { console.log("已取消删除") });

    },

    // 选中所有子菜单
    checkedAllChildMenu(checked){
        let childMenu = [...this.permitRoutes[this.parentIndex].children]
        
        for (const iterator of childMenu) {

            if(checked){

                if(!this.checkedMenus.includes(iterator.menuUrl))
                   this.checkedMenus.push(iterator.menuUrl)
              
            }else{
                let newSet = new Set(this.checkedMenus)
                
                newSet.delete(iterator.menuUrl)
                this.checkedMenus = [...newSet]
            }
        }
        
    },

    //选中子菜单
    checkedChildMenu(checked){
        let parentMenu = this.permitRoutes[this.parentIndex].menuUrl

        if(!this.checkedMenus.includes(parentMenu) && checked)
            this.checkedMenus.push(parentMenu)
    }
  }

    
}
</script>

<style lang="scss" scoped>
.page-bar{text-align:center;margin-top:15px;}
    .user-table{
        margin-top:20px;
    }
    .menu-box{margin-bottom:26px;position: relative;}
    .menu-permit-list ul{padding-left:0px;}
    .menu-permit-list ul li{
     list-style:none;
     height:25px;
     line-height:25px;
    }
    .children-menu-permit{padding-left:20px;margin:10px 0px;position: relative;}
    .children-menu-permit >ul >li{float:left;}
    .clear-float{clear: both;}
    .permit-list{margin-left:35px;}
    .children-menu{width:150px;border-right:1px solid #ccc;}
    .parent-menu{width:170px;border-right:1px solid #ccc;}
    .permit-header{height:35px;line-height:25px;border-bottom: medium double #ccc;}
    .permit-header li{float:left;width:50%;list-style:none;font-size:14px;font-weight:bold;padding:8px 0px 8px 30px;}
    .el-checkbox {color: #5e729c;}
    .permit-list .el-checkbox{color:rgb(27, 207, 183);}
    li.parent-permit-list{position:absolute;top:0px;left:170px;}
</style>
