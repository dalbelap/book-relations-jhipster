'use strict';

angular.module('bidarApp')
    .factory('Relation', function ($resource, DateUtils) {
        return $resource('api/relations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
