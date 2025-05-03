from flask import Flask, request, jsonify
from database import get_connection
from lxd_manager import create_container, stop_container, delete_container
import os

app = Flask(__name__)

@app.route('/')
def home():
    return "Nestworld API is running!"

@app.route('/create', methods=['POST'])
def create():
    data = request.json
    owner_uuid = data.get('owner_uuid')
    friends = data.get('friends', [])
    if not owner_uuid:
        return jsonify({"error": "Missing owner_uuid"}), 400
    container_name = owner_uuid.replace('-', '')
    try:
        create_container(container_name)
        conn = get_connection()
        with conn.cursor() as cursor:
            sql = "INSERT INTO islands (container_name, owner_uuid, friends, created_at, last_active, delete_after_days) VALUES (%s, %s, %s, NOW(), NOW(), 30)"
            cursor.execute(sql, (container_name, owner_uuid, ",".join(friends)))
        conn.commit()
        return jsonify({"status": "success", "container": container_name})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/stop', methods=['POST'])
def stop():
    data = request.json
    container_name = data.get('container')
    if not container_name:
        return jsonify({"error": "Missing container"}), 400
    try:
        stop_container(container_name)
        return jsonify({"status": "stopped"})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/delete_if_inactive', methods=['POST'])
def delete_if_inactive():
    try:
        conn = get_connection()
        with conn.cursor() as cursor:
            sql = "SELECT container_name, last_active, delete_after_days FROM islands"
            cursor.execute(sql)
            rows = cursor.fetchall()
            for row in rows:
                container, last_active, delete_after_days = row
                # Check inactivity time
                import datetime
                delta = datetime.datetime.now() - last_active
                if delta.days >= delete_after_days:
                    delete_container(container)
                    cursor.execute("DELETE FROM islands WHERE container_name = %s", (container,))
            conn.commit()
        return jsonify({"status": "cleanup complete"})
    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8080)
