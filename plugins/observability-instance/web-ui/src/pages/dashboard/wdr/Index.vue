<script setup lang="ts">
import { useRequest } from "vue-request";
import moment from "moment";
import restRequest from "../../../request/restful";
import BuildWdr from "./build_wdr.vue";
import SnapshotManage from "./snapshot_manage.vue";
import { cloneDeep } from "lodash-es";

const errorInfo = ref<string | Error>();

const props = withDefaults(
    defineProps<{
        instanceId?: string;
    }>(),
    {
        instanceId: "",
    }
);

// nodeId sync
const emit = defineEmits(["nodeIdChanged"]);
const clusterComponent = ref(null);
const culsterLoaded = ref<boolean>(false);
const initNodeId = ref<string>("");
const syncNodeId = (syncNodeIdVal: string) => {
    if (!culsterLoaded.value) initNodeId.value = syncNodeIdVal;
    else {
        clusterComponent.value.setNodeId(syncNodeIdVal);
        if (syncNodeIdVal !== cluster.value[1]) {
            Object.assign(page, { pageSize: page.pageSize, total: 0, currentPage: 1 });
            nextTick(() => {
                requestData();
            });
        }
    }
};
defineExpose({
    syncNodeId,
});

const initFormData = {
    reportRange: "CLUSTER",
    reportType: "DETAIL",
    dateValue: [moment(new Date()).format("YYYY-MM-DD") + " 00:00:00", moment(new Date()).format("YYYY-MM-DD") + " 23:59:59"],
};
const formData = reactive(cloneDeep(initFormData));
const tableData = ref<Array<any>>([]);

const page = reactive({
    currentPage: 1,
    pageSize: 10,
    total: 10,
});
const snapshotManageShown = ref(false);
const buildWDRShown = ref(false);
const showSnapshotManage = () => {
    snapshotManageShown.value = true;
};
const changeModalSnapshotManage = (val: boolean) => {
    snapshotManageShown.value = val;
};
const showBuildWDR = () => {
    buildWDRShown.value = true;
};
const changeModalBuildWDR = (val: boolean) => {
    buildWDRShown.value = val;
};
const bandleCoveyBuildWDR = (code: number) => {
    requestData();
};
const handleQuery = () => {
    requestData();
};
const handleReset = () => {
    formData.reportRange = initFormData.reportRange;
    formData.reportType = initFormData.reportType;
    formData.dateValue = initFormData.dateValue;
    requestData();
};

const cluster = ref<Array<any>>([]);
const handleClusterValue = (val: any) => {
    cluster.value = val;
    emit("nodeIdChanged", cluster.value[1]);
};
const clusterLoaded = (val: any) => {
    culsterLoaded.value = true;
    if (initNodeId.value) clusterComponent.value.setNodeId(initNodeId.value);
    nextTick(() => {
        requestData();
    });
};
const {
    data: res,
    run: requestData,
    loading,
} = useRequest(
    () => {
        const clusterId = cluster.value.length ? cluster.value[0] : "";
        return restRequest
            .get("/wdr/list", {
                clusterId,
                wdrScope: formData.reportRange,
                wdrType: formData.reportType,
                start: formData.dateValue && formData.dateValue.length > 0 ? formData.dateValue[0] : null,
                end: formData.dateValue && formData.dateValue.length > 1 ? formData.dateValue[1] : null,
                pageSize: page.pageSize,
                pageNum: page.currentPage,
            })
            .then(function (res) {
                return res;
            })
            .catch(function (res) {
                tableData.value = [];
                Object.assign(page, { pageSize: page.pageSize, total: 0, currentPage: 1 });
            });
    },
    { manual: true }
);
type Res =
    | {
          records: string[];
          pageNum: number;
          total: number;
      }
    | undefined;
watch(res, (res: Res) => {
    if (res && res.records && res.records.length) {
        const { total } = res;
        tableData.value = res.records;
        Object.assign(page, { pageSize: page.pageSize, total });
    } else {
        tableData.value = [];
    }
});
const handleSizeChange = (val: number) => {
    page.currentPage = 1;
    page.pageSize = val;
    changePageCurrent(page.currentPage);
};
const handleCurrentChange = (val: number) => {
    page.currentPage = val;
    changePageCurrent(page.currentPage);
};
const changePageCurrent = (data: number) => {
    Object.assign(page, data);
    requestData();
};

// view WDR
type Row = {
    wdrId: string;
    reportName: string;
};
const { run: handleView, loading: viewing } = useRequest(
    (row: Row) => {
        return restRequest
            .get("/wdr/downloadWdr", {
                wdrId: row?.wdrId,
            })
            .then(function (res) {
                const newWindow = window.open(row.reportName, "_blank");
                newWindow?.document.write(res.data);
            })
            .catch(function (res) {});
    },
    { manual: true }
);

// download WDR
const { run: handleDownload, loading: downloading } = useRequest(
    (row: Row) => {
        return restRequest
            .get("/wdr/downloadWdr", {
                wdrId: row?.wdrId,
            })
            .then(function (res) {
                if (res.data) {
                    const blob = new Blob([res.data], {
                        type: "text/plain",
                    });
                    const a = document.createElement("a");
                    const URL = window.URL || window.webkitURL;
                    const herf = URL.createObjectURL(blob);
                    a.href = herf;
                    a.download = row.reportName;
                    document.body.appendChild(a);
                    a.click();
                    document.body.removeChild(a);
                    window.URL.revokeObjectURL(herf);
                }
            })
            .catch(function (res) {
                tableData.value = [];
                Object.assign(page, { pageSize: page.pageSize, total: 0, currentPage: 1 });
            });
    },
    { manual: true }
);

// delete row
const { run: hanleDelete, loading: deleting } = useRequest(
    (row: Row) => {
        return restRequest
            .delete(`/wdr/del/${row.wdrId}`)
            .then(function (res) {
                requestData();
            })
            .catch(function (res) {});
    },
    { manual: true }
);
</script>

<template>
    <div class="top-sql">
        <div class="tab-wrapper-container">
            <div class="search-form-multirow">
                <div class="row">
                    <div class="filter">
                        <ClusterCascader ref="clusterComponent" @loaded="clusterLoaded" notClearable :title="$t('dashboard.wdrReports.clusterName')" @getCluster="handleClusterValue" />
                    </div>
                    <div class="filter">
                        <span>{{ $t("dashboard.wdrReports.reportRange") }}&nbsp;</span>
                        <el-select v-model="formData.reportRange" style="width: 160px; margin: 0 4px">
                            <el-option value="CLUSTER" :label="$t('dashboard.wdrReports.reportRangeSelect[0]')" />
                            <el-option value="NODE" :label="$t('dashboard.wdrReports.reportRangeSelect[1]')" />
                        </el-select>
                    </div>
                    <div class="filter">
                        <span>{{ $t("dashboard.wdrReports.reportType") }}&nbsp;</span>
                        <el-select v-model="formData.reportType" style="width: 160px; margin: 0 4px">
                            <el-option value="DETAIL" :label="$t('dashboard.wdrReports.reportTypeSelect[0]')" />
                            <el-option value="SUMMARY" :label="$t('dashboard.wdrReports.reportTypeSelect[1]')" />
                            <el-option value="ALL" :label="$t('dashboard.wdrReports.reportTypeSelect[2]')" />
                        </el-select>
                    </div>

                    <div class="filter">
                        <span>{{ $t("dashboard.wdrReports.buildTime") }}&nbsp;</span>
                        <MyDatePicker v-model="formData.dateValue" :teleported="true" type="datetimerange" style="width: 300px" />
                    </div>
                </div>

                <div class="row">
                    <div class="filter">
                        <el-button @click="handleQuery">{{ $t("app.query") }}</el-button>
                        <el-button @click="handleReset">{{ $t("app.reset") }}</el-button>
                        <el-button type="primary" @click="showSnapshotManage">{{ $t("dashboard.wdrReports.snapshotManage") }}</el-button>
                        <el-button type="primary" @click="showBuildWDR">{{ $t("dashboard.wdrReports.buildWDR") }}</el-button>
                    </div>
                </div>
            </div>
        </div>

        <div class="page-container">
            <div class="table-wrapper" v-loading="loading || viewing || downloading || deleting">
                <el-table class="normal-table" :data="tableData" :header-cell-style="{ 'text-align': 'center' }" style="width: 100%" :default-sort="{ prop: 'date', order: 'descending' }">
                    <el-table-column prop="scope" :label="$t('dashboard.wdrReports.reportRange')" width="80" align="center" />
                    <el-table-column prop="reportAt" :label="$t('dashboard.wdrReports.list.buildTime')" width="180" align="center" />
                    <el-table-column prop="reportType" :label="$t('dashboard.wdrReports.reportType')" width="80" align="center" />
                    <el-table-column prop="reportName" :label="$t('dashboard.wdrReports.list.reportName')" width="420" align="center" />
                    <el-table-column :label="$t('app.operate')" align="center" fixed="right" width="130">
                        <template #default="scope">
                            <div class="operate-btns">
                                <el-link size="small" type="primary" @click="handleView(scope.row)">{{ $t("app.view") }}</el-link>
                                <el-link size="small" type="primary" @click="handleDownload(scope.row)">{{ $t("app.download") }}</el-link>
                                <el-popconfirm title="Are you sure to delete this?" @confirm="hanleDelete(scope.row)">
                                    <template #reference>
                                        <el-link size="small" type="primary">{{ $t("app.delete") }}</el-link>
                                    </template>
                                </el-popconfirm>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <el-pagination :currentPage="page.currentPage" :pageSize="page.pageSize" :total="page.total" :page-sizes="[10, 20, 30, 40]" class="pagination" layout="total,sizes,prev,pager,next" background small @size-change="handleSizeChange" @current-change="handleCurrentChange" />

        <SnapshotManage :show="snapshotManageShown" @changeModal="changeModalSnapshotManage" />
        <BuildWdr :show="buildWDRShown" @changeModal="changeModalBuildWDR" @conveyFlag="bandleCoveyBuildWDR" />

        <my-message v-if="errorInfo" type="error" :tip="errorInfo" defaultTip="" />
    </div>
</template>

<style scoped lang="scss">
@import "../../../assets/style/style1.scss";
</style>