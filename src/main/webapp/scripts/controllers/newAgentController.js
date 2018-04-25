
angular.module('canneLogger').controller('NewAgentController', function ($scope, $location, locationParser, flash, AgentResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.agent = $scope.agent || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            flash.setMessage({'type':'success','text':'The agent was created successfully.'});
            $location.path('/Agents');
        };
        var errorCallback = function(response) {
            if(response && response.data) {
                flash.setMessage({'type': 'error', 'text': response.data.message || response.data}, true);
            } else {
                flash.setMessage({'type': 'error', 'text': 'Something broke. Retry, or cancel and start afresh.'}, true);
            }
        };
        AgentResource.save($scope.agent, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Agents");
    };
});