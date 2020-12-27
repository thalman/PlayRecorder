package net.halman.playrecorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class Promotion {
    private final static String APP_ID = "net.halman.playrecorder"; // Package Name
    private final static int DIALOG_FREQUENCY = 14;
    private final static int DIALOG_MIN_COUNT = 5;
    private final static int DIALOG_FIRST_SHOW_AFTER = 30;
    private boolean _rate_provided = false;
    private int _rate_dialog_show_count = 0;
    private boolean _support_provided = false;
    private int _support_dialog_show_count = 0;
    private int _promote_show_date = 0;
    private static final int PROMOTE_RATE = 0;
    private static final int PROMOTE_SUPPORT = 1;
    private int _promote_show_dialog = PROMOTE_RATE;
    private Context _context;

    public Promotion(Context context)
    {
        _context = context;
        loadConfig(_context);
    }

    private int unixDay()
    {
        return (int)(System.currentTimeMillis() / 1000L / 3600L / 24L);
    }

    private void loadConfig(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("playrecorderpromotion", 0);
        _rate_provided             = prefs.getBoolean("rate-provided", false);
        _rate_dialog_show_count    = prefs.getInt("rate-dialog-show-count", 0);
        _support_dialog_show_count = prefs.getInt("support-dialog-show-count", 0);
        _support_provided          = prefs.getBoolean("support-provided", false);
        _promote_show_date         = prefs.getInt("promote-show-date", unixDay() + DIALOG_FIRST_SHOW_AFTER);
        _promote_show_dialog       = prefs.getInt("promote-show-dialog", PROMOTE_RATE);

        String app_name = _context.getResources().getString(R.string.app_name);
        _support_provided = _support_provided || app_name.contains("+");
    }

    private void saveConfig(Context context)
    {
        SharedPreferences prefs = context.getSharedPreferences("playrecorderpromotion", 0);
        SharedPreferences.Editor editor = prefs.edit();
        if (editor != null) {
            editor.putBoolean("rate-provided", _rate_provided);
            editor.putInt("rate-dialog-show-count", _rate_dialog_show_count );
            editor.putBoolean("support-provided", _support_provided);
            editor.putInt("support-dialog-show-count", _support_dialog_show_count);
            editor.putInt("promote-show-date", _promote_show_date);
            editor.putInt("promote-show-dialog", _promote_show_dialog);
            editor.apply();
        }
    }

    private void rate(final Context context)
    {
        if (_rate_provided) {
            return;
        }

        _rate_dialog_show_count++;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.promoteRate);
        builder.setMessage(R.string.promoteRateDetail);
        builder.setPositiveButton(R.string.promoteRateNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _rate_provided = true;
                saveConfig(context);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_ID)));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.promoteRateLater, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        if (_rate_dialog_show_count > DIALOG_MIN_COUNT) {
            builder.setNeutralButton(R.string.promoteRateNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _rate_provided = true;
                    saveConfig(context);
                    dialog.dismiss();
                }
            });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                _rate_provided = false;
                saveConfig(context);
            }
        });

        builder.show();
    }

    private void support(final Context context)
    {
        if (_support_provided) {
            return;
        }

        _support_dialog_show_count++;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.promoteSupport);
        builder.setMessage(R.string.promoteSupportDetail);
        builder.setPositiveButton(R.string.promoteSupportNow, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                _promote_show_dialog = unixDay() + 60;
                saveConfig(context);
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_ID + "plus")));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.promoteRateLater, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        if (_support_dialog_show_count > DIALOG_MIN_COUNT) {
            builder.setNeutralButton(R.string.promoteRateNo, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _support_provided = true;
                    saveConfig(context);
                    dialog.dismiss();
                }
            });
        }

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                saveConfig(context);
            }
        });

        builder.show();
    }

    public void promote()
    {
        if (_rate_provided && _support_provided) {
            return;
        }

        if (_promote_show_date + DIALOG_FREQUENCY > unixDay()) {
            return;
        }
        _promote_show_date = unixDay();

        if (_rate_provided) {
            _promote_show_dialog = PROMOTE_SUPPORT;
        }

        switch (_promote_show_dialog) {
            case PROMOTE_RATE:
                _promote_show_dialog = PROMOTE_SUPPORT;
                rate(_context);
                break;
            case PROMOTE_SUPPORT:
                _promote_show_dialog = PROMOTE_RATE;
                support(_context);
                break;
            default:
                _promote_show_dialog = PROMOTE_RATE;
                saveConfig(_context);
                break;
        }
    }
}
