<template>
  <div class="table-page">
    <el-tabs v-model="currentTabName" class="tabs" @tab-click="handleClick">
      <el-tab-pane label="DDL" name="DDL" />
      <el-tab-pane :label="$t('table.data.title')" name="Data" />
    </el-tabs>
    <el-icon class="refresh" @click="handleRefresh"><Refresh /></el-icon>
    <div class="table-container">
      <KeepAlive>
        <component
          :is="dataMap[currentTabName].component"
          :data="dataMap[currentTabName].data"
          :loading="dataMap[currentTabName].loading"
        ></component>
      </KeepAlive>
    </div>
  </div>
</template>

<script lang="ts" setup>
  import { markRaw, onMounted, reactive, ref } from 'vue';
  import type { TabsPaneContext } from 'element-plus';
  import { useRoute } from 'vue-router';
  import { useUserStore } from '@/store/modules/user';
  import DDL from './components/DDL.vue';
  import Data from './components/Data.vue';
  import { getViewDdls, getViewDatas } from '@/api/view';
  import { debounce, formatTableV2Data } from '@/utils';

  const route = useRoute();
  const UserStore = useUserStore();
  const commonParams = reactive({
    connectionName: '',
    schema: '',
    viewName: '',
    webUser: UserStore.userId,
    uuid: '',
  });
  const dataMap = reactive({
    DDL: {
      data: '',
      component: markRaw(DDL),
      hasLoad: false,
      loading: false,
    },
    Data: {
      data: [],
      component: markRaw(Data),
      hasLoad: false,
      loading: false,
    },
  });

  const currentTabName = ref('DDL');

  const handleClick = (tab: TabsPaneContext) => {
    if (dataMap[tab.paneName].hasLoad) return;
    getData(tab.paneName);
  };

  const getData = async (type) => {
    const api = {
      DDL: getViewDdls,
      Data: getViewDatas,
    };
    if (!Object.keys(api).includes(type)) return;
    dataMap[type].loading = true;
    api[type](commonParams)
      .then((res) => {
        if (type == 'DDL') {
          dataMap.DDL.data = res;
        } else {
          const { column, result } = res;
          dataMap[type].data = formatTableV2Data(column, result);
        }
      })
      .finally(() => {
        dataMap[type].hasLoad = true;
        dataMap[type].loading = false;
      });
  };

  const doFreshDebounce = debounce(
    () => {
      getData(currentTabName.value);
    },
    1000,
    true,
  );
  const handleRefresh = () => {
    if (!currentTabName.value) return;
    doFreshDebounce();
  };

  onMounted(() => {
    const { connectInfoName, viewName, schema, uuid } = route.query;
    Object.assign(commonParams, {
      connectionName: connectInfoName,
      viewName,
      schema,
      uuid,
    });
    getData(currentTabName.value);
  });
</script>
<style lang="scss" scoped>
  .table-page {
    height: 100%;
    padding: 10px 20px;
    position: relative;
  }
  .tabs {
    position: relative;
    :deep(.el-tabs__content) {
      padding: 0;
      color: #6b778c;
      font-size: 32px;
      font-weight: normal;
    }
  }
  .refresh {
    position: absolute;
    right: 18px;
    top: 15px;
    cursor: pointer;
    &:hover {
      color: var(--hover-color);
    }
  }
  .table-container {
    height: calc(100% - 40px);
  }
  :deep(.el-table) {
    height: 100%;
  }
</style>
