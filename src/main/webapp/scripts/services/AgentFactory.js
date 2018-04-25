angular.module('canneLogger').factory('AgentResource', function($resource){
    var resource = $resource('rest/agents/:AgentId',{AgentId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});