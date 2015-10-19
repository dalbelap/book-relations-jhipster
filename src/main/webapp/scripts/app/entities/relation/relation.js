'use strict';

angular.module('relacionesApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('relation', {
                parent: 'entity',
                url: '/relations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'relacionesApp.relation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/relation/relations.html',
                        controller: 'RelationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('relation');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('relation.detail', {
                parent: 'entity',
                url: '/relation/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'relacionesApp.relation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/relation/relation-detail.html',
                        controller: 'RelationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('relation');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Relation', function($stateParams, Relation) {
                        return Relation.get({id : $stateParams.id});
                    }]
                }
            })
            .state('relation.new', {
                parent: 'relation',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/relation/relation-dialog.html',
                        controller: 'RelationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('relation', null, { reload: true });
                    }, function() {
                        $state.go('relation');
                    })
                }]
            })
            .state('relation.edit', {
                parent: 'relation',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/relation/relation-dialog.html',
                        controller: 'RelationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Relation', function(Relation) {
                                return Relation.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('relation', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
