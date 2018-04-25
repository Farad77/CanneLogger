'use strict';

angular.module('canneLogger',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Agents',{templateUrl:'views/Agent/search.html',controller:'SearchAgentController'})
      .when('/Agents/new',{templateUrl:'views/Agent/detail.html',controller:'NewAgentController'})
      .when('/Agents/edit/:AgentId',{templateUrl:'views/Agent/detail.html',controller:'EditAgentController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
