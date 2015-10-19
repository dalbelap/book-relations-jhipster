/* globals $ */
'use strict';

angular.module('relacionesApp')
    .directive('relacionesAppPager', function() {
        return {
            templateUrl: 'scripts/components/form/pager.html'
        };
    });
