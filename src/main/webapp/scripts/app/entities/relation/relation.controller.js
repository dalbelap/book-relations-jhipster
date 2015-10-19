'use strict';

angular.module('relacionesApp')
    .controller('RelationController', function ($scope, Relation, RelationSearch, ParseLinks) {
        $scope.relations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Relation.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.relations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Relation.get({id: id}, function(result) {
                $scope.relation = result;
                $('#deleteRelationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Relation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRelationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            RelationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.relations = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.relation = {
                code: null,
                date: null,
                author: null,
                title: null,
                testimonial: null,
                digitalSampleUrl: null,
                digitalSampleFile: null,
                digitalSampleFileContentType: null,
                digitalSampleFile2: null,
                digitalSampleFile2ContentType: null,
                id: null
            };
        };

        $scope.abbreviate = function (text) {
            if (!angular.isString(text)) {
                return '';
            }
            if (text.length < 30) {
                return text;
            }
            return text ? (text.substring(0, 15) + '...' + text.slice(-10)) : '';
        };

        $scope.byteSize = function (base64String) {
            if (!angular.isString(base64String)) {
                return '';
            }
            function endsWith(suffix, str) {
                return str.indexOf(suffix, str.length - suffix.length) !== -1;
            }
            function paddingSize(base64String) {
                if (endsWith('==', base64String)) {
                    return 2;
                }
                if (endsWith('=', base64String)) {
                    return 1;
                }
                return 0;
            }
            function size(base64String) {
                return base64String.length / 4 * 3 - paddingSize(base64String);
            }
            function formatAsBytes(size) {
                return size.toString().replace(/\B(?=(\d{3})+(?!\d))/g, " ") + " bytes";
            }

            return formatAsBytes(size(base64String));
        };
    });
