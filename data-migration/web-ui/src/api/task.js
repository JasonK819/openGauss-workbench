import axios from 'axios'

export function clustersData (payload) {
  return axios.get('/plugins/data-migration/resource/getClusters', payload)
}

export function hostsData () {
  return axios.get('/plugins/data-migration/resource/getHosts')
}

export function sourceClusterDbsData (query) {
  return axios.get('/plugins/data-migration/resource/getSourceClusterDbs', { params: query })
}

export function targetClusterDbsData (query) {
  return axios.get('/plugins/data-migration/resource/getTargetClusterDbs', { params: query })
}

export function migrationSave (payload) {
  return axios.post('/plugins/data-migration/migration/save', payload)
}

export function migrationUpdate (payload) {
  return axios.post('/plugins/data-migration/migration/update', payload)
}

export function defaultParams () {
  return axios.get('/plugins/data-migration/param/default')
}