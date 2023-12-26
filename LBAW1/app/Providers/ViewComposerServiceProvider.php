<?php

namespace App\Providers;

use Illuminate\Support\Facades\View;
use Illuminate\Support\ServiceProvider;
use App\Models\Notification;
use Illuminate\Support\Facades\Auth;

class ViewComposerServiceProvider extends ServiceProvider
{
    public function boot()
    {
        View::composer('app', function ($view) {
            if (Auth::check()) {
                $notifications = Notification::where('user_id', Auth::user()->id)->get();
                $view->with('notifications', $notifications);
            }
        });
    }
}
