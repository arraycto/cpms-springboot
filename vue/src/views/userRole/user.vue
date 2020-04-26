<template>
  <div class="app-container">
    <el-button
      type="primary"
      @click="handleAddUser"
      size="mini"
      v-if="checkPermission('/user/add/**')"
    >
      <i class="el-icon-circle-plus-outline"></i>&nbsp;添加用户
    </el-button>
    <div class="user-table">
      <el-table :data="allUser" border style="width: 100%">
        <el-table-column prop="userId" align="center" label="序号" type="index" width="180"></el-table-column>
        <el-table-column prop="userName" align="center" label="姓名" width="180"></el-table-column>
        <el-table-column prop="roleName" align="center" label="角色"></el-table-column>
        <el-table-column prop="createTime" label="创建日期" align="center" width="180"></el-table-column>
        <el-table-column prop="handle" align="center" label="操作">
          <template slot-scope="scope">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(scope.$index, allUser)"
              v-if="checkPermission('/user/edit/**')"
            >编辑</el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(scope.$index, allUser)"
              v-if="checkPermission('/user/delete/**')"
            >删除</el-button>
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

    <el-dialog :visible.sync="dialogVisible" width="35%" :title="dialogType==='add'?'添加用户':'编辑用户'">
      <el-form
        ref="userInfo"
        :model="userInfo"
        :rules="userRules"
        label-width="80px"
        label-position="left"
      >
        <el-form-item label="用户名" prop="name">
          <el-input v-model="userInfo.name" placeholder="用户名..." />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-show="dialogType==='add'" >
          <el-input v-model="userInfo.password" type="password" autocomplete="new-password"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPwd" v-show="dialogType==='add'">
          <el-input v-model="userInfo.confirmPwd" type="password" />
        </el-form-item>
        <el-form-item label="所属角色" prop="selectedRoles">
          <el-select
            v-model="userInfo.selectedRoles"
            multiple
            placeholder="请选择"
            style="width:100%;"
          >
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <input type="hidden" name="userIndex" id="userIndex" value v-model="userIndex" />
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmAddUser" v-if="dialogType==='add'">确认</el-button>
        <el-button type="primary" @click="confirmEditUser" v-else>确认</el-button>
      </div>
    </el-dialog>

	
  </div>
  
</template>

<script>
import { getAllUser, addUser, editUser, deleteUser } from "@/api/user";
import { getAllRole } from "@/api/role";
import { Message } from "element-ui";
import { checkPermission } from "@/utils/index";
const defaultUser = {
  userId: null,
  name: "",
  password: "",
  confirmPwd: "",
  roleIds: "",
  selectedRoles: [] // 选中的角色id
};

export default {
  name: "user",
  data() {
    const validateUsername = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error("请填写用户名"));
      } else {
        callback();
      }
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length == 0) {
        callback(new Error("请填写密码"));
      } else {
        callback();
      }
    };

    const validateConfirmPwd = (rule, value, callback) => {
      if (this.userInfo.confirmPwd != this.userInfo.password) {
        callback(new Error("确认密码不一致"));
      } else {
        callback();
      }
    };
    return {
      userInfo: {
        userId: null,
        name: "",
        password: "",
        confirmPwd: "",
        roleIds: "",
        selectedRoles: [] // 选中的角色id
	  },
	  currentPage:1,
	  pageSize:10,
	  count:null,
      userRules: {
        name: [
          { required: true, trigger: "blur", validator: validateUsername }
        ],
        password: [
          { required: true, trigger: "blur", validator: validatePassword }
        ],
        confirmPwd: [
          {
            required: true,
            trigger: ["blur", "change"],
            validator: validateConfirmPwd
          }
        ],
        selectedRoles: [
          { required: true, trigger: "change", message: "请选择用户角色" }
        ]
      },
      dialogType: "add",
      dialogVisible: false,
      roles: [],
      allUser: [],
      userIndex: ""
    };
  },

  inject: ["reload"],

  created() {
	  this.getUserList()
  },

  methods: {
    checkPermission,
    confirmAddUser() {
      this.$refs["userInfo"].validate(valid => {
        if (valid) {
          this.userInfo.roleIds = this.userInfo.selectedRoles.join(",");

          addUser(this.userInfo).then(res => {
            if (res.code == 1000) {
              this.roles = res.data;
              Message({
                message: res.msg || "success",
                type: "success"
              });

              this.reload();
            } else {
              Message({
                message: res.msg || "warning",
                type: "warning"
              });
            }
          });
        }
      });
      return;
	},
	getUserList(){
		getAllUser({page:this.currentPage,pageSize:this.pageSize}).then(res => {
			if (res.code == 1000) {

				this.allUser = res.data.lists
				this.count   = res.data.totalCount

			} else {
				Message({
					message: res.msg || "warning",
					type: "warning"
				});
			}
		});
	},
    handleCurrentChange(val) {
		this.currentPage = val
      	this.getUserList()
    },
    confirmEditUser() {
      if (this.userInfo.name.trim().length <= 0) {
        Message({
          message: "请填写用户名",
          type: "warning"
        });
        return;
      }

      if (this.userInfo.selectedRoles.length <= 0) {
        Message({
          message: "请选择用户角色",
          type: "warning"
        });
        return;
      }

      let editInfo = {
        roleIds: this.userInfo.selectedRoles.join(","),
        name: this.userInfo.name,
        userId: this.userInfo.userId
      };

      editUser(editInfo).then(res => {
        if (res.code == 1000) {
          Message({
            message: res.msg || "success",
            type: "success"
          });

          this.reload(); // 刷新页面
          this.resetForm();
        } else {
          Message({
            message: res.msg || "error",
            type: "warning"
          });
        }
      });
    },

    handleAddUser() {
      this.dialogVisible = true;
      this.dialogType = "add";
      this.resetForm();

      this.getAllRoles()
    },
    getAllRoles() {
       getAllRole().then(res => {
        if (res.code == 1000) {
          this.roles = res.data.lists;
        }
      })
    },
    resetForm() {
      if (this.$refs.userInfo !== undefined) {
        this.$nextTick(() => {
          if (this.$refs.userInfo !== undefined) {
            this.$refs.userInfo.resetFields();
          }
        });
      }
    },
    clearFormValidate() {
      if (this.$refs.userInfo !== undefined) {
        this.$refs.userInfo.clearValidate();
      }
    },

    handleEdit(index, rows) {
      this.dialogVisible = true;
      this.dialogType = "edit";
      this.clearFormValidate();
      let info = rows[index];
      this.userInfo.name = info.userName;
      this.userInfo.userId = info.userId;
      this.userInfo.selectedRoles = info.roleIds.split(",").map(Number); // 字符串数组 转 -> 数字数组
	  this.userIndex = index;
	  
	  this.getAllRoles()
	},

    handleDelete(index, rows) {
      let delUserName = rows[index].userName;
      this.$confirm(`确定要删除[${delUserName}], 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          deleteUser({ userId: rows[index].userId }).then(res => {
            if (res.code == 1000) {
              Message({
                message: res.msg || "success",
                type: "success"
              });
            } else {
              Message({
                message: res.msg || "success",
                type: "warning"
              });
            }
          });

          this.reload();
        })
        .catch(() => {
          console.log("已取消删除");
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.page-bar{text-align:center;margin-top:15px;}
.user-table {
  margin-top: 20px;
}
</style>
